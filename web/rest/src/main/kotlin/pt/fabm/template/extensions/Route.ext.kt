package pt.fabm.template.extensions

import Consts
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpHeaders
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.auth.AbstractUser
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.web.impl.CookieImpl
import io.vertx.reactivex.ext.auth.User
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.Route
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.CookieHandler
import pt.fabm.template.ErrorResponse
import pt.fabm.template.rest.RestResponse
import pt.fabm.template.rest.RestVerticle
import pt.fabm.template.validation.AuthException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

typealias ToSingleRestResponse = (RoutingContext) -> Single<RestResponse>

val LOGGER = LoggerFactory.getLogger(RestVerticle::class.java)!!

fun errorResolver(error: Throwable, applyResponse: (RestResponse) -> Unit) {
  when (error) {
    is CompositeException -> {
      errorResolver(error.exceptions[error.size() - 1], applyResponse)
    }
    is ErrorResponse -> applyResponse(error.toRestResponse())
    else -> {
      LOGGER.error("technical error", error)
      applyResponse(errorToRestResponse(error, 500))
    }
  }

}

private fun consumeRest(rc: RoutingContext, restResponse: Single<RestResponse>) {

  val response = rc.response()
  val applyResponse: (RestResponse) -> Unit = { rest ->
    response.statusCode = rest.statusCode
    response.end(rest.buffer)
  }

  restResponse.subscribe({ rest ->
    applyResponse(rest)
  }, { error ->
    errorResolver(error, applyResponse)
  })
}

fun Route.withBody(): Route {
  return this.handler(BodyHandler.create())
}

fun Route.withCookies(): Route {
  return this.handler(CookieHandler.create())
}

/**
 * handle single rest response
 */
fun Route.handlerSRR(handler: ToSingleRestResponse): Route {

  val mainHandler = Handler<RoutingContext> { rc ->
    consumeRest(rc, Single.just(rc).flatMap(handler))
  }
  return this.handler(mainHandler)
}

fun Route.authEval(userTimeout: Long): Route =

  this.handler { rc ->
    val user = object : AbstractUser() {
      override fun doIsPermitted(permission: String?, resultHandler: Handler<AsyncResult<Boolean>>) {
        resultHandler.handle(Future.succeededFuture(false))
      }

      override fun setAuthProvider(authProvider: AuthProvider?) {
        //ignore
      }

      override fun principal(): JsonObject = JsonObject().put("user", "unknown")

    }

    val cookieHeader = rc.request().headers().get(HttpHeaders.COOKIE)
    if (cookieHeader == null) {
      rc.setUser(User(user))
      rc.next()
      return@handler
    }

    val allCookies = ServerCookieDecoder.STRICT.decode(cookieHeader).map { Cookie.newInstance(CookieImpl(it)) }
    try {
      val cookie = allCookies.find { cookie -> cookie.name == Consts.ACCESS_TOKEN_COOKIE }

      if (cookie == null) {
        rc.setUser(User(user))
        rc.next()
        return@handler
      }

      LOGGER.trace("login in ${Instant.now()} with token ${cookie.value}")

      Jwts.parser()
        .setSigningKey(Consts.SIGNING_KEY)
        .parseClaimsJws(cookie.value).body
    } catch (e: ExpiredJwtException) {
      LOGGER.trace("token expired")
      val datePlusTimeout = LocalDateTime
        .now()
        .plus(userTimeout, ChronoUnit.MILLIS)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .let { Date.from(it) }

      val jws = Jwts.builder()
        .setSubject(e.claims.subject)
        .signWith(Consts.SIGNING_KEY)
        .setExpiration(datePlusTimeout)
        .compact()

      LOGGER.trace("new token $jws")

      val cookie = Cookie.cookie(Consts.ACCESS_TOKEN_COOKIE, jws)
      cookie.setHttpOnly(true)
      rc.addCookie(cookie)
      rc.setUser(User(user))
      e.claims
    } catch (e: Exception) {
      e.printStackTrace()
      rc.setUser(User(user))
      rc.next()
      return@handler
    }.let {
      val user = object : AbstractUser() {
        override fun doIsPermitted(permission: String?, resultHandler: Handler<AsyncResult<Boolean>>) {
          if (permission.equals("global")) resultHandler.handle(Future.succeededFuture(true))
          else resultHandler.handle(Future.succeededFuture(false))
        }

        override fun setAuthProvider(authProvider: AuthProvider?) {
          //ignore
        }

        override fun principal(): JsonObject = JsonObject().put("user", it.subject)

      }
      rc.setUser(io.vertx.reactivex.ext.auth.User(user))
    }
  }


fun Route.authHandler(userTimeout: Long, handler: (AuthContext) -> Single<RestResponse>): Route {

  val toAuthContext = { rc: RoutingContext ->
    val cookieHeader = rc.request().headers().get(HttpHeaders.COOKIE) ?: throw AuthException()
    val allCookies = ServerCookieDecoder.STRICT.decode(cookieHeader).map { Cookie.newInstance(CookieImpl(it)) }
    val claims = try {
      val cookie = allCookies.find { cookie -> cookie.name == Consts.ACCESS_TOKEN_COOKIE } ?: throw AuthException()
      LOGGER.trace("login in ${Instant.now()} with token ${cookie.value}")

      Jwts.parser()
        .setSigningKey(Consts.SIGNING_KEY)
        .parseClaimsJws(cookie.value).body
    } catch (e: ExpiredJwtException) {
      LOGGER.trace("token expired")
      val datePlusTimeout = LocalDateTime
        .now()
        .plus(userTimeout, ChronoUnit.MILLIS)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .let { Date.from(it) }

      val jws = Jwts.builder()
        .setSubject(e.claims.subject)
        .signWith(Consts.SIGNING_KEY)
        .setExpiration(datePlusTimeout)
        .compact()

      LOGGER.trace("new token $jws")

      val cookie = Cookie.cookie(Consts.ACCESS_TOKEN_COOKIE, jws)
      cookie.setHttpOnly(true)
      rc.addCookie(cookie)
      e.claims
    } catch (e: AuthException) {
      throw e
    } catch (e: Exception) {
      throw AuthException()
    }

    handler(AuthContext(claims, rc))
  }

  val mainHandler = Handler<RoutingContext> { rc ->
    val singleRestResponse = Single.just(rc).flatMap(toAuthContext)
    consumeRest(rc, singleRestResponse)
  }

  return this.handler(mainHandler)
}
