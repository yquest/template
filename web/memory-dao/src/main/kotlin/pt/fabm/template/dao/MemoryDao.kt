package pt.fabm.template.dao

import io.reactivex.Completable
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.json.get
import io.vertx.reactivex.core.AbstractVerticle
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.template.models.UserRegisterIn

class MemoryDao : AbstractVerticle() {
  companion object {
    val LOGGER = LoggerFactory.getLogger(MemoryDao::class.java)
  }

  private val cars = mutableListOf<Car>()
  private val users = mutableMapOf<String, UserRegisterIn>()

  override fun rxStart(): Completable {
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val eventBus = vertx.eventBus()
    val completeHandeleres = mutableListOf<Completable>()

    completeHandeleres += eventBus.consumer<Car>("dao.car.create") { message ->
      val carMessage: Car = message.body()
      val car: Car? = cars.find { it.make == carMessage.make && it.model == carMessage.model }
      if (car != null) {
        message.fail(1,"already exists")
        return@consumer
      }
      cars += message.body()
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<Car>("dao.car.update") { message ->
      val car: Car = message.body()
      val isRemoved = cars.removeIf { it.make == car.make && it.model == car.model }
      if (!isRemoved) {
        message.fail(1,"not found")
        return@consumer
      }
      cars.add(car)
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<List<Car>>("dao.car.list") { message ->
      message.reply(cars, DeliveryOptions().setCodecName("List"))
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<UserRegisterIn>("dao.user.create") { message ->
      val body = message.body()
      users[body.name] = body
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<JsonObject>("dao.car.retrieve") { message ->
      val body = message.body()
      val car: Car? = cars.find { it.make == body["make"] && it.model == body["model"] }
      message.fail(1,"already exists")
      message.reply(car)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<JsonObject>("dao.car.delete") { message ->
      val body = message.body()
      cars.removeIf { it.make.name == body["make"] && it.model == body["model"] }
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<JsonObject>("dao.user.login") { message ->
      val body = message.body()
      val current = users.get(body.getString("user"))

      val auth = current?.takeIf {
        val argPass = body.getBinary("pass")
        argPass!!.contentEquals(it.pass)
      } != null
      message.reply(auth)
    }.rxCompletionHandler()

    return Completable.merge(completeHandeleres)
  }
}
