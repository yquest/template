package pt.fabm.template.extensions

import Consts
import com.github.benmanes.caffeine.cache.Caffeine
import io.jsonwebtoken.Jwts
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import io.vertx.core.Handler
import io.vertx.core.http.HttpHeaders
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.web.impl.CookieImpl
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.Route
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.CookieHandler
import pt.fabm.template.ErrorResponse
import pt.fabm.template.rest.RestResponse
import pt.fabm.template.validation.AuthContext
import pt.fabm.template.validation.AuthException
import java.util.concurrent.TimeUnit

typealias ToSingleRestResponse = (RoutingContext) -> Single<RestResponse>

val LOGGER = LoggerFactory.getLogger(Route::class.java)
val cachedUsers = Caffeine
  .newBuilder()
  .expireAfterAccess(30, TimeUnit.MINUTES)
  .build<String, Boolean>()


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

fun Route.authHandler(handler: (AuthContext) -> Single<RestResponse>): Route {

  val toAuthContext = { rc: RoutingContext ->
    val cookieHeader = rc.request().headers().get(HttpHeaders.COOKIE) ?: throw AuthException()
    val allCookies = ServerCookieDecoder.STRICT.decode(cookieHeader).map { Cookie.newInstance(CookieImpl(it)) }
    val cookie = allCookies.find { cookie -> cookie.name == Consts.ACCESS_TOKEN_COOKIE } ?: throw AuthException()
    val claims = Jwts.parser()
      .setSigningKey(Consts.SIGNING_KEY)
      .parseClaimsJws(cookie.value)

    val subject = Jwts.parser()
      .setSigningKey(Consts.SIGNING_KEY)
      .parseClaimsJws(cookie.value)
      .body
      .subject ?: AuthException()

    val user = cachedUsers.getIfPresent(subject) ?: throw AuthException()
    handler(AuthContext(claims,rc))
  }

  val mainHandler = Handler<RoutingContext> { rc ->
    val singleRestResponse = Single.just(rc).flatMap(toAuthContext)
    consumeRest(rc, singleRestResponse)
  }

  return this.handler(mainHandler)
}
