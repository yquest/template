package pt.fabm.template.dao

import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarId

interface CarDao {

  /**
   * ### method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.create]
   * ### reply: none
   * errors: 1 = Already exists
   */
  fun create(message: Message<Car>)

  /**
   * ### method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.update]
   * ### reply: none
   * errors: 1 = Not found
   */
  fun update(message: Message<Car>)

  /**
   * ### method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.list]
   * ### reply: List<[Car]>
   */
  fun list(message: Message<Unit>)

  /**
   * ### method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.retrieve]
   * ### reply: [Car]
   * errors: 1 = Not found
   */
  fun find(message: Message<CarId>)

  /**
   * ### method called in address [pt.fabm.template.EventBusAddresses.Dao.Car.delete]
   * ### reply: none
   */
  fun delete(message: Message<CarId>)
}
