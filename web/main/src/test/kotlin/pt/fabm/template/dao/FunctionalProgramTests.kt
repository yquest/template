package pt.fabm.template.dao

import io.reactivex.Observable
import io.vertx.core.Handler
import io.vertx.junit5.Timeout
import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pt.fabm.template.extensions.userTimers
import java.util.concurrent.TimeUnit

@ExtendWith(VertxExtension::class)
class FunctionalProgramTests {
  @Test
  fun filterList(vertx: Vertx, testContext: VertxTestContext) {
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
  @DisplayName("test timer")
  @Timeout(value = 10, timeUnit = TimeUnit.SECONDS)
  @Throws(Throwable::class)
  fun testTimer(vertx: Vertx, testContext: VertxTestContext) {
    val username = "abcd"
    var counter = 0
    val timerCallback = Handler<Long> { idTimer ->
      println("do checkpoint 3 with timerId:${idTimer}")
      userTimers.remove(username)
      assertEquals(7,counter)
      testContext.completeNow()
    }

    fun doOnLogin(firstTime: Boolean = false) {
      userTimers.computeIfPresent(username) { _, idTimer ->
        if (firstTime) throw IllegalStateException("unexpected call!!")
        println("do checkpoint 1 with timerId:${idTimer}")
        vertx.cancelTimer(idTimer)
        counter++
        vertx.setTimer(1000, timerCallback)
      }

      userTimers.computeIfAbsent(username) { username ->
        println("do checkpoint 2")
        counter++
        vertx.setTimer(2000, timerCallback)
      }

    }

    doOnLogin(true)
    (1..6).forEach {
      //do after 1 seconds
      vertx.setTimer((it*500).toLong()) {
        doOnLogin()
      }
    }
  }


}
