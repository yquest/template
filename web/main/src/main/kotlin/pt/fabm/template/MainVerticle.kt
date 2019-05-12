package pt.fabm.template

import io.reactivex.Completable
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.AbstractVerticle
import pt.fabm.template.models.Car
import pt.fabm.template.models.Reservation
import pt.fabm.template.models.SimpleDate
import pt.fabm.template.models.UserRegisterIn
import java.lang.Exception


class MainVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(MainVerticle::class.java)
  }

  private fun <T> registerLocalCodec(klass: Class<T>) {
    vertx.eventBus().delegate.registerDefaultCodec(klass, LocalCodec(klass))
  }

  private fun registerCodecs() {
    registerLocalCodec(UserRegisterIn::class.java)
    registerLocalCodec(SimpleDate::class.java)
    registerLocalCodec(Reservation::class.java)
    registerLocalCodec(Car::class.java)
    registerLocalCodec(java.util.List::class.java)
  }

  override fun rxStart(): Completable {
    registerCodecs()

    val store = ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(config())

    val retriever = ConfigRetriever.create(
      vertx,
      ConfigRetrieverOptions().addStore(store)
    )

    return retriever.rxGetConfig().flatMapCompletable { conf ->
      deployVerticles(conf).doOnError { error ->
        LOGGER.error("Verticles load error", Exception(error))
      }
    }

  }

  private fun deployVerticles(config: JsonObject): Completable {
    val verticles = config.getJsonObject("verticles")
    val restVerticle = verticles.getString("rest")
    val daoVerticle = verticles.getString("dao")
    val confs = config.getJsonObject("confs")
    val restConf = confs.getJsonObject("rest")
    val daoConf = confs.getJsonObject("dao")

    return vertx
      .rxDeployVerticle(daoVerticle, DeploymentOptions().setConfig(daoConf))
      .ignoreElement()
      .andThen(vertx.rxDeployVerticle(restVerticle, DeploymentOptions().setConfig(restConf)).ignoreElement())
  }

}
