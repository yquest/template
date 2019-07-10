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

    testContext.checkpoint(2)

    val timerCallback = Handler<Long> {
      userTimers.remove(username)
      testContext.completeNow()
    }

    userTimers.computeIfPresent(username) { _, _ ->
      throw IllegalStateException("unexpected call!!")
    }

    userTimers.compute(username) { _, _ ->
      vertx.setTimer(1000 * 30, timerCallback)
    }

    //do after 2 seconds
    vertx.setTimer(2000) {
      userTimers.computeIfPresent(username) { _, id ->
        vertx.cancelTimer(id)
        testContext.checkpoint()
        id
      }

      userTimers.compute(username) { _, _ ->
        testContext.checkpoint()
        vertx.setTimer(1000, timerCallback)
      }
    }

  }



}
