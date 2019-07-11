package pt.fabm.template.extensions

import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toHash() = MessageDigest.getInstance("SHA-512").digest(this.toByteArray())!!
infix fun String.passMatches(hash: ByteArray) = this.toHash().contentEquals(hash)
fun String?.nullIfEmpty(): String? = this?.takeIf { it.isNotEmpty() }
infix fun <E : Enum<E>> String.toEnum(e: Class<E>): E? = this.toEnumArray(e.enumConstants)
infix fun <E : Enum<E>> String.toEnumArray(e: Array<E>): E? {
  for (constant in e) {
    if (constant.name == this) return constant
  }
  return null
}

fun String.toLocalDateTime(): LocalDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
