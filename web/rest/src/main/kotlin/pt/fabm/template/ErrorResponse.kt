package pt.fabm.template

import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.buffer.Buffer
import pt.fabm.template.rest.RestResponse

interface ErrorResponse {
  companion object {
    fun toRestResponse(message: String?, statusCode: Int): RestResponse {
      return RestResponse(
        Buffer.newInstance(JsonObject().put("error", message).toBuffer()),
        statusCode
      )
    }

    fun toRestResponse(error: Throwable, statusCode: Int): RestResponse = toRestResponse(error.message, statusCode)
  }

  val statusCode: Int
    get() = 500
  val message: String?
  fun toRestResponse(): RestResponse = toRestResponse(message, statusCode)
}
