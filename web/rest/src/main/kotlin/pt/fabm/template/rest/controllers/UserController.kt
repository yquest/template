package pt.fabm.template.rest.controllers

import Consts
import io.jsonwebtoken.Jwts
import io.reactivex.Single
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.eventbus.Message
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.LOGGER
import pt.fabm.template.extensions.checkedString
import pt.fabm.template.extensions.toHash
import pt.fabm.template.models.type.Login
import pt.fabm.template.models.type.UserRegisterIn
import pt.fabm.template.rest.RestResponse
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class UserController(val vertx: Vertx, private val userTimeout: Long) {
  fun userLogout(rc: RoutingContext){
    rc.getCookie(Consts.ACCESS_TOKEN_COOKIE)?.setPath("/api/")?.setMaxAge(0L)
    return Single.just(RestResponse())
  }

  fun userLogin(rc: RoutingContext){
    val body = rc.bodyAsJson
    val login = Login(
      username = body.getString("user"),
      password = body.getString("pass").toHash()
    )

    fun messageSent(message: Message<Boolean>): RestResponse {
      if (!message.body()) {
        return RestResponse(statusCode = 403)
      }
      val username = login.username

      val datePlusTimeout = LocalDateTime
        .now()
        .plus(userTimeout,ChronoUnit.MILLIS)
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .let { Date.from(it) }

      val jws = Jwts.builder()
        .setSubject(username)
        .setExpiration(datePlusTimeout)
        .signWith(Consts.SIGNING_KEY)
        .compact()

      LOGGER.trace("expiring in ${datePlusTimeout.toInstant()}:$jws")

      var cookie = Cookie.cookie(Consts.ACCESS_TOKEN_COOKIE, jws)
      cookie.setHttpOnly(true)
      cookie.path = "/api/"
      rc.addCookie(cookie)

      cookie = Cookie.cookie(Consts.USER_NAME_COOKIE, username)
      cookie.path = "/api/*"
      rc.addCookie(cookie)
      return RestResponse(statusCode = 200)
    }

    return vertx.eventBus()
      .rxSend<Boolean>(EventBusAddresses.Dao.User.login, login)
      .map(::messageSent)
  }

  fun createUser(rc: RoutingContext) {
    val bodyAsJson = rc.bodyAsJson
    val name: String = bodyAsJson.checkedString(UserRegisterIn.NAME)
    val email: String = bodyAsJson.checkedString(UserRegisterIn.EMAIL)
    val password: String = bodyAsJson.checkedString(UserRegisterIn.PASS)

    val userRegister = UserRegisterIn(
      name,
      email,
      password.toHash()
    )

    return vertx.eventBus().rxSend<String>(EventBusAddresses.Dao.User.create, userRegister)
      .map {
        RestResponse(statusCode = 204)
      }
  }
}
