package pt.fabm.template.dao

import io.reactivex.Completable
import io.vertx.core.shareddata.Shareable
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.shareddata.LocalMap
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.models.Car
import pt.fabm.template.models.UserRegisterIn

class DaoVerticle : AbstractVerticle() {

  override fun rxStart(): Completable {
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val eventBus = vertx.eventBus()
    val userDao = UserDaoMemory()
    val carDao = CarDaoMemory()

    DaoMemoryShared.cars = carDao.cars
    DaoMemoryShared.users = userDao.users

    return Completable.mergeArray(
      eventBus.consumer(EventBusAddresses.Dao.Car.create, carDao::create).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.update, carDao::update).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.list, carDao::list).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.retrieve, carDao::find).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.delete, carDao::delete).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.User.create, userDao::create).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.User.login, userDao::login).rxCompletionHandler()
    )
  }
}
