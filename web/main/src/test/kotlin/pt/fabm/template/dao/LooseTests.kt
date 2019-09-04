package pt.fabm.template.dao

import Consts
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.reactivex.Observable
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import org.junit.Assert
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@ExtendWith(VertxExtension::class)
class LooseTests {

  interface Element {
    fun render(builder: StringBuilder, indent: String)
  }

  class TextElement(val text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
      builder.append("$indent$text\n")
    }
  }

  @DslMarker
  annotation class HtmlTagMarker

  @HtmlTagMarker
  abstract class Tag(val name: String) : Element {
    val children = arrayListOf<Element>()
    val attributes = hashMapOf<String, String>()

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
      tag.init()
      children.add(tag)
      return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
      builder.append("$indent<$name${renderAttributes()}>\n")
      for (c in children) {
        c.render(builder, indent + "  ")
      }
      builder.append("$indent</$name>\n")
    }

    private fun renderAttributes(): String {
      val builder = StringBuilder()
      for ((attr, value) in attributes) {
        builder.append(" $attr=\"$value\"")
      }
      return builder.toString()
    }

    override fun toString(): String {
      val builder = StringBuilder()
      render(builder, "")
      return builder.toString()
    }
  }

  abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
      children.add(TextElement(this))
    }
  }

  class HTML : TagWithText("html") {
    fun head(init: Head.() -> Unit) = initTag(Head(), init)

    fun body(init: Body.() -> Unit) = initTag(Body(), init)
  }

  class Head : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
  }

  class Title : TagWithText("title")

  abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTag(B(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun a(href: String, init: A.() -> Unit) {
      val a = initTag(A(), init)
      a.href = href
    }
  }

  class Body : BodyTag("body")
  class B : BodyTag("b")
  class P : BodyTag("p")
  class H1 : BodyTag("h1")

  class A : BodyTag("a") {
    var href: String
      get() = attributes["href"]!!
      set(value) {
        attributes["href"] = value
      }
  }

  fun html(create: HTML.() -> Unit): HTML {
    val html = HTML()
    html.create()
    return html
  }

  @Test
  fun testDsl(testContext: VertxTestContext) {
    html {

    }
    testContext.completeNow()
  }

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
      .plus(2000, ChronoUnit.MILLIS)
      .atZone(ZoneId.systemDefault())
      .toInstant()
      .let { Date.from(it) }

    val initialDate = datePlus2Seconds()

    fun createToken(date: Date, user: String): String = Jwts.builder()
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
