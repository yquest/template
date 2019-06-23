package pt.fabm.template.dao

import io.reactivex.Completable
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.models.*

class DaoVerticle : AbstractVerticle() {
  companion object {
    val LOGGER = LoggerFactory.getLogger(DaoVerticle::class.java)
  }

  private val cars = mutableListOf<Car>()
  private val users = mutableMapOf<String, UserRegisterIn>()

  override fun rxStart(): Completable {
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val eventBus = vertx.eventBus()
    val completeHandeleres = mutableListOf<Completable>()

    completeHandeleres += eventBus.consumer<Car>(EventBusAddresses.Dao.Car.create) { message ->
      val carMessage: Car = message.body()
      val car: Car? = cars.find { it.make == carMessage.make && it.model == carMessage.model }
      if (car != null) {
        message.fail(1,"already exists")
        return@consumer
      }
      cars += message.body()
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<Car>(EventBusAddresses.Dao.Car.update) { message ->
      val car: Car = message.body()
      val isRemoved = cars.removeIf { it.make == car.make && it.model == car.model }
      if (!isRemoved) {
        message.fail(1,"not found")
        return@consumer
      }
      cars.add(car)
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<List<Car>>(EventBusAddresses.Dao.Car.list) { message ->
      message.reply(cars, DeliveryOptions().setCodecName("List"))
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<UserRegisterIn>(EventBusAddresses.Dao.User.create) { message ->
      val body = message.body()
      users[body.name] = body
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<CarId>(EventBusAddresses.Dao.Car.retrieve) { message ->
      val body = message.body()
      val car: Car? = cars.find { it.make == body.maker && it.model == body.model }
      message.fail(1,"already exists")
      message.reply(car)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<CarId>(EventBusAddresses.Dao.Car.delete) { message ->
      val body = message.body()
      cars.removeIf { it.make == body.maker && it.model == body.model }
      message.reply(null)
    }.rxCompletionHandler()

    completeHandeleres += eventBus.consumer<Login>(EventBusAddresses.Dao.User.login) { message ->
      val body = message.body()
      val current = users.get(body.username)

      val auth = current?.takeIf {
        body.password.contentEquals(it.pass)
      } != null
      message.reply(auth)
    }.rxCompletionHandler()

    return Completable.merge(completeHandeleres)
  }
}
