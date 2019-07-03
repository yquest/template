package pt.fabm.template.dao

import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarId

interface CarDao {
  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.create]
   *
   * reply: none
   */
  fun create(message: Message<Car>)

  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.update]
   *
   * reply: none
   */
  fun update(message: Message<Car>)

  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.list]
   *
   * reply: List<[Car]>
   */
  fun list(message: Message<Unit>)

  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.retrieve]
   *
   * reply: [Car]
   */
  fun find(message: Message<CarId>)

  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.delete]
   *
   * reply: none
   */
  fun delete(message: Message<CarId>)
}
