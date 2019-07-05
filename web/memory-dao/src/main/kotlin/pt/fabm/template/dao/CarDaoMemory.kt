package pt.fabm.template.dao

import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarId

class CarDaoMemory : CarDao {

  val cars = mutableListOf<Car>()

  override fun create(message: Message<Car>) {
    val carMessage: Car = message.body()
    val car: Car? = cars.find { it.make == carMessage.make && it.model == carMessage.model }
    if (car != null) {
      message.fail(1, "already exists")
      return
    }
    cars += message.body()
    message.reply(null)
  }

  override fun update(message: Message<Car>) {
    val car: Car = message.body()
    val isRemoved = cars.removeIf { it.make == car.make && it.model == car.model }
    if (!isRemoved) {
      message.fail(1, "not found")
      return
    }
    cars.add(car)
    message.reply(null)
  }

  override fun list(message: Message<Unit>) {
    message.reply(cars, DeliveryOptions().setCodecName("List"))
  }

  override fun find(message: Message<CarId>) {
    val body = message.body()
    val car: Car? = cars.find { it.make == body.maker && it.model == body.model }
    message.fail(1, "not found")
    message.reply(car)
  }

  override fun delete(message: Message<CarId>) {
    val body = message.body()
    cars.removeIf { it.make == body.maker && it.model == body.model }
    message.reply(null)
  }

}
