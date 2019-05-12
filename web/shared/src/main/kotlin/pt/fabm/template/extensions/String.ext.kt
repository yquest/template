package pt.fabm.template.extensions

import java.security.MessageDigest

fun String.toHash() = MessageDigest.getInstance("SHA-512").digest(this.toByteArray())!!
infix fun String.passMatches(hash:ByteArray) = this.toHash().contentEquals(hash)
fun String?.nullIfEmpty():String? = this?.takeIf { it.isNotEmpty() }
