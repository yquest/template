package pt.fabm.template.rest

import io.netty.buffer.Unpooled
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.core.http.HttpServerResponse

data class RestResponse(
  val buffer: Buffer = Buffer.newInstance(io.vertx.core.buffer.Buffer.buffer(Unpooled.EMPTY_BUFFER)),
  val statusCode: Int = 200
) {
  constructor(jsonObject: JsonObject, statusCode: Int = 200) : this(
    Buffer.newInstance(jsonObject.toBuffer()),
    statusCode
  )

  constructor(jsonArray: JsonArray, statusCode: Int = 200) : this(
    Buffer.newInstance(jsonArray.toBuffer()),
    statusCode
  )

  fun handle(response: HttpServerResponse) {
    response.statusCode = statusCode
    response.end(buffer)
  }
}
