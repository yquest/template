package pt.fabm.template.extensions

import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.buffer.Buffer
import pt.fabm.template.ErrorResponse
import pt.fabm.template.rest.RestResponse

fun errorToRestResponse(message: String?, statusCode: Int): RestResponse {
  return RestResponse(
    Buffer.newInstance(JsonObject().put("error", message).toBuffer()),
    statusCode
  )
}

fun errorToRestResponse(error: Throwable, statusCode: Int): RestResponse =
  errorToRestResponse(error.message, statusCode)


fun ErrorResponse.toRestResponse():RestResponse{
  return errorToRestResponse(message,statusCode)
}
