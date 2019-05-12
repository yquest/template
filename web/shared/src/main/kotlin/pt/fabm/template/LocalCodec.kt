package pt.fabm.template

import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec

class LocalCodec <T>(var klass: Class<T>) : MessageCodec<T, T> {
  override fun decodeFromWire(pos: Int, buffer: Buffer?): T {
    throw IllegalStateException("only implemented in remote purposes")
  }

  override fun systemCodecID(): Byte {
    return (-1).toByte()
  }

  override fun encodeToWire(buffer: Buffer?, s: T) {
    throw IllegalStateException("only implemented in remote purposes")
  }

  override fun transform(s: T): T {
    return s
  }

  override fun name(): String {
    return klass.simpleName
  }

}


