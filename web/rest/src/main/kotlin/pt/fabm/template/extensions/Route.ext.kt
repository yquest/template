package pt.fabm.template.extensions

import io.vertx.reactivex.ext.web.Route
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.CookieHandler

fun Route.withBody(): Route {
  return this.handler(BodyHandler.create())
}

fun Route.withCookies(): Route {
  return this.handler(CookieHandler.create())
}
