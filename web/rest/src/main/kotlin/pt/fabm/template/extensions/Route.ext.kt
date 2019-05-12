package pt.fabm.template.extensions

import Consts
import io.jsonwebtoken.Jwts
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.reactivex.Single
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
import kotlin.Function as Function1001

typealias ToSingleRestResponse = (RoutingContext) -> Single<RestResponse>

val LOGGER = LoggerFactory.getLogger(Route::class.java)

private fun consumeRest(rc: RoutingContext, restResponse: Single<RestResponse>) {

  val response = rc.response()

  val applyResponse: (RestResponse) -> Unit = { rest ->
    response.statusCode = rest.statusCode
    response.end(rest.buffer)
  }

  restResponse.subscribe({ rest ->
    applyResponse(rest)
  }, { error ->
    when (error) {
      is ErrorResponse -> applyResponse(error.toRestResponse())
      else -> {
        LOGGER.error("technical error", error)
        applyResponse(ErrorResponse.toRestResponse(error, 500))
      }
    }
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
  val mainHandler = Handler<RoutingContext> { rc ->
    //get cookie header
    val singleRestResponse = Single.just(rc)
      //get all cookies
      .map {
        it.request().headers().get(HttpHeaders.COOKIE) ?: throw AuthException()
      }.toObservable()
      .flatMapIterable { cookieHeader -> ServerCookieDecoder.STRICT.decode(cookieHeader) }
      //transform netty cookies in vertx cookies
      .map { cookie -> Cookie.newInstance(CookieImpl(cookie)) }
      //filter cookies with access token
      .filter { cookie ->
        cookie.name == Consts.ACCESS_TOKEN_COOKIE
      }
      //first cookie which matches with access_token name or throws NoSuchElement
      .firstOrError()
      .map { cookie ->
        try {
          Jwts.parser()
            .setSigningKey(Consts.SIGNING_KEY)
            .parseClaimsJws(cookie.value)
        } catch (e: Exception) {
          throw AuthException()
        }
      }.map { claims -> AuthContext(claims, rc) }
      .flatMap { handler(it) }

    consumeRest(rc, singleRestResponse)
  }

  return this.handler(mainHandler)
}
