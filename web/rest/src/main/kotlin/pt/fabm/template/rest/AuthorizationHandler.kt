package pt.fabm.template.rest

import Consts
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.vertx.core.http.HttpHeaders
import io.vertx.ext.web.impl.CookieImpl
import io.vertx.reactivex.ext.web.Cookie
import io.vertx.reactivex.ext.web.RoutingContext
import pt.fabm.template.validation.AuthException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

interface AuthorizationHandler {
  val timeout: Long get() = DEFAULT_TIMEOUT
  val rc: RoutingContext
  val claims: Claims
    get() {
      val cookieHeader = rc.request().headers().get(HttpHeaders.COOKIE) ?: throw AuthException()
      val allCookies = ServerCookieDecoder.STRICT.decode(cookieHeader)
        .map {
          Cookie.newInstance(CookieImpl(it))
        }

      return try {
        val cookie = allCookies.find { cookie -> cookie.name == Consts.ACCESS_TOKEN_COOKIE } ?: throw AuthException()

        Jwts.parser()
          .setSigningKey(Consts.SIGNING_KEY)
          .parseClaimsJws(cookie.value).body
      } catch (e: ExpiredJwtException) {
        val datePlusTimeout = LocalDateTime
          .now()
          .plus(timeout, ChronoUnit.MILLIS)
          .atZone(ZoneId.systemDefault())
          .toInstant()
          .let { Date.from(it) }

        val jws = Jwts.builder()
          .setSubject(e.claims.subject)
          .signWith(Consts.SIGNING_KEY)
          .setExpiration(datePlusTimeout)
          .compact()

        val cookie = Cookie.cookie(Consts.ACCESS_TOKEN_COOKIE, jws)
        cookie.setHttpOnly(true)
        rc.addCookie(cookie)
        e.claims
      } catch (e: AuthException) {
        throw e
      } catch (e: Exception) {
        throw AuthException()
      }
    }

  companion object {
    const val DEFAULT_TIMEOUT: Long = 48/*hours*/ * 60/*minutes*/ * 60/*seconds*/ * 1000/*millis*/
    const val USER_NAME:String = "user"
    const val PASSWORD:String = "password"

    fun getClaims(rc: RoutingContext): AuthorizationHandler = object : AuthorizationHandler {
      override val rc: RoutingContext = rc
    }
  }
}
