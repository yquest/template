package pt.fabm

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pt.fabm.template.extensions.toHash

class StringExtensionTest {

  @Test
  fun testHashLoader(){
    val hash = "hello extensions".toHash()
    Assertions.assertTrue("hello extensions".toHash().contentEquals(hash))
    Assertions.assertFalse(!"another extensions".toHash().contentEquals(hash))
  }
}
