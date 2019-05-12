package pt.fabm.template

import io.reactivex.Completable
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.eventbus.MessageConsumer

class EBForwarderVerticleTest : AbstractVerticle() {
  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(EBForwarderVerticleTest::class.java)
  }

  override fun rxStart(): Completable {
    val ebConsumers = config().getJsonArray("eb_consumers")
    return ebConsumers.map { el ->
      messageConsumer(el.toString(),config()
        .getJsonObject("codecs")
        .getString(el.toString())
      ).rxCompletionHandler()
    }.let {
      Completable.merge(it)
    }
  }

  private fun  messageConsumer(address: String, codec:String?): MessageConsumer<Any> {
    val eventBus = vertx.eventBus()
    val consumerUserCreate = eventBus
      .consumer<Any>(address)

    consumerUserCreate.handler { message ->
      eventBus.rxSend<Any>("test.$address", message.body(), DeliveryOptions().setCodecName(codec))
        .subscribe({ fw ->
          message.reply(fw.body(), DeliveryOptions().setCodecName(codec))
        }, { error ->
          LOGGER.error("Error on message:$address", error)
        })
    }
    return consumerUserCreate
  }
}
