package pt.fabm.template.rest.controllers

import Consts
import io.jsonwebtoken.Jwts
import io.reactivex.Single
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.cachedUsers
import pt.fabm.template.extensions.checkedString
import pt.fabm.template.extensions.toHash
import pt.fabm.template.models.Login
import pt.fabm.template.models.UserRegisterIn
import pt.fabm.template.rest.RestResponse

class UserController(val vertx: Vertx) {
  fun userLogout(rc: RoutingContext): Single<RestResponse> {
    rc.getCookie(Consts.ACCESS_TOKEN_COOKIE)?.setPath("/api/")?.setMaxAge(0L)
    return Single.just(RestResponse())
  }

  fun userLogin(rc: RoutingContext): Single<RestResponse> = Single.just(rc).map {
    val body = rc.bodyAsJson
    Login(
      username = body.getString("user"),
      password = body.getString("pass").toHash()
    )
  }.flatMap { login ->
    vertx.eventBus()
      .rxSend<Boolean>(EventBusAddresses.Dao.User.login, login)
      .map { message ->
        if (!message.body()) {
          return@map RestResponse(statusCode = 403)
        }
        val username = login.username
        val jws = Jwts.builder()
          .setSubject(username)
          .signWith(Consts.SIGNING_KEY)
          .compact()
        var cookie = Cookie.cookie(Consts.ACCESS_TOKEN_COOKIE, jws)
        cookie.setHttpOnly(true)
        cookie.path = "/api/"
        rc.addCookie(cookie)

        cookie = Cookie.cookie(Consts.USER_NAME_COOKIE, username);
        cookie.path = "/api/*"
        rc.addCookie(cookie)
        cachedUsers.put(login.username, true)
        RestResponse(statusCode = 200)
      }
  }

  fun createUser(rc: RoutingContext): Single<RestResponse> {
    return Single.just(rc).map { it.response() }
      .flatMap {
        val bodyAsJson = rc.bodyAsJson
        val name: String = bodyAsJson.checkedString("name")
        val email: String = bodyAsJson.checkedString("email")
        val password: String = bodyAsJson.checkedString("password")

        val userRegister = UserRegisterIn(
          name,
          email,
          password.toHash()
        )

        vertx.eventBus().rxSend<String>(EventBusAddresses.Dao.User.create, userRegister)
          .map {
            RestResponse(statusCode = 204)
          }
      }
  }
}
