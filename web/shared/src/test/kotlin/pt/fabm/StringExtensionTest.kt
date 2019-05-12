package pt.fabm

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pt.fabm.template.extensions.passMatches
import pt.fabm.template.extensions.toHash

class StringExtensionTest {

  @Test
  fun testHashLoader(){
    val hash = "hello extensions".toHash()
    Assertions.assertTrue("hello extensions" passMatches hash)
    Assertions.assertFalse("another extensions" passMatches hash)
  }
}
