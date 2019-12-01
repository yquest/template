package pt.fabm.template.extensions

import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toHash() = MessageDigest.getInstance("SHA-512").digest(this.toByteArray())!!
infix fun String.passMatches(hash: ByteArray) = this.toHash().contentEquals(hash)
