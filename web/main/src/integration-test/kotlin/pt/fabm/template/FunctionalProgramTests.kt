package pt.fabm.template

import io.reactivex.Observable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FunctionalProgramTests {
  @Test
  fun filterList() {
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

  }


}
