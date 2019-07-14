package pt.fabm.template.dao

import Consts
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.reactivex.Observable
import io.vertx.core.Handler
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class LooseTests {
  @Test
  fun filterList(testContext: VertxTestContext) {
    var count = 0
    val array = arrayListOf("1a", "1b", "1c")
    var str = array.map { str ->
      count++
      str.substring(1)
    }.find { str ->
      str == "b"
    }

    assertEquals(3, count)
    assertEquals("b", str)
    count = 0

    str = Observable.fromIterable(array)
      .map { s ->
        count++
        s.substring(1)
      }.filter { s ->
        s == "b"
      }.firstElement()
      .blockingGet()
    assertEquals(2, count)
    assertEquals("b", str)
    testContext.completeNow()
  }

  @Test
  fun checkTokenExpirationDate(vertx: Vertx, testContext: VertxTestContext) {

    val user = "xico"

    fun datePlus2Seconds() = LocalDateTime
      .now()
      .plus(2000,ChronoUnit.MILLIS)
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .let { Date.from(it) }

    val initialDate = datePlus2Seconds()

    fun createToken(date:Date, user:String):String = Jwts.builder()
        .setSubject(user)
        .setExpiration(date)
        .signWith(Consts.SIGNING_KEY)
        .compact()


    val firstToken = createToken(initialDate, user)

    val asyncCheck = testContext.checkpoint(3)
    vertx.setTimer(500) {
      println(firstToken)
      val claims = Jwts.parser()
        .setSigningKey(Consts.SIGNING_KEY)
        .parseClaimsJws(firstToken)

      Assert.assertEquals(user, claims.body.subject)
      Assert.assertTrue(claims.body.expiration.toInstant().isAfter(Instant.now()))
      asyncCheck.flag()
    }

    vertx.setTimer(3000) {
      try {
        val claims = Jwts.parser()
          .setSigningKey(Consts.SIGNING_KEY)
          .parseClaimsJws(firstToken)
        Assert.assertEquals(user, claims.body.subject)
        Assert.assertTrue(claims.body.expiration.toInstant().isBefore(Instant.now()))
        Assert.fail()
      } catch (e: ExpiredJwtException) {
        Assert.assertEquals(user, e.claims.subject)
        asyncCheck.flag()
      }
      val currentDate = datePlus2Seconds()
      Assert.assertNotEquals(firstToken, createToken(currentDate, user))
      asyncCheck.flag()
    }

  }

}
