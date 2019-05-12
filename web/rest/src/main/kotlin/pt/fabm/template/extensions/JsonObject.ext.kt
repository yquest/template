package pt.fabm.template.extensions

import io.vertx.core.json.JsonObject
import pt.fabm.template.validation.RequiredException

fun JsonObject.checkedString(key: String): String =
  this.getString(key) ?: throw RequiredException(key)


fun JsonObject.checkedInt(key: String): Int =
  this.getInteger(key) ?: throw RequiredException(key)


fun JsonObject.checkedJsonObject(key: String): JsonObject =
  this.getJsonObject(key) ?: throw RequiredException(key)

