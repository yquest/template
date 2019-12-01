package pt.fabm.template.rest.controllers

import Consts
import io.jsonwebtoken.Jwts
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.toHash
import pt.fabm.template.models.type.Login
import pt.fabm.template.models.type.UserRegisterIn
import pt.fabm.template.rest.AuthorizationHandler
import pt.fabm.template.validation.RequiredException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

class UserController(val vertx: Vertx, private val userTimeout: Long) {
  companion object{
    val LOGGER = LoggerFactory.getLogger(UserController::class.java)
  }
  fun userLogout(rc: RoutingContext){
    rc.getCookie(Consts.ACCESS_TOKEN_COOKIE)?.setPath("/api/")?.setMaxAge(0L)
    rc.response().end()
  }

  fun userLogin(rc: RoutingContext){
    val body = rc.bodyAsJson
    val login = Login(
      username = body.getString(UserRegisterIn.NAME),
      password = body.getString(UserRegisterIn.PASS).toHash()
    )

    fun notAuth() {
      rc.response().statusCode = 403
      rc.response().end()
    }

    fun messageSent(isAuth: Boolean) {
      if (!isAuth) return notAuth()
      val username = login.username

      val datePlusTimeout = LocalDateTime
        .now()
        .plus(userTimeout, ChronoUnit.MILLIS)
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
      rc.response().statusCode = 200
      rc.response().end()
    }

    fun handleError(error: Throwable) {
      error.printStackTrace()
      return notAuth()
    }

    vertx.eventBus()
      .rxSend<Boolean>(EventBusAddresses.Dao.User.login, login)
      .map { it.body() }
      .subscribe(::messageSent, ::handleError)
  }

  fun createUser(rc: RoutingContext) {
    val bodyAsJson = rc.bodyAsJson
    val name: String? = bodyAsJson.getString(UserRegisterIn.NAME)
    val email: String? = bodyAsJson.getString(UserRegisterIn.EMAIL)
    val password: String? = bodyAsJson.getString(UserRegisterIn.PASS)

    if (name.isNullOrEmpty()) throw RequiredException(UserRegisterIn.NAME)
    if (email.isNullOrEmpty()) throw RequiredException(UserRegisterIn.EMAIL)
    if (password.isNullOrEmpty()) throw RequiredException(UserRegisterIn.PASS)

    val userRegister = UserRegisterIn(
      name,
      email,
      password.toHash()
    )

    fun onError(error: Throwable) {
      LOGGER.error(error)
      rc.response().end()
    }

    fun onSuccess(token: String) {
      rc.response().end(
        JsonObject().put("token", token)
          .toBuffer().let { Buffer(it) }
      )
    }

    vertx.eventBus()
      .rxSend<String>(EventBusAddresses.Dao.User.create, userRegister)
      .map { it.body() }
      .subscribe(::onSuccess, ::onError)
  }
}
