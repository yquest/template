package pt.fabm.template.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import io.reactivex.Completable
import io.reactivex.SingleObserver
import io.reactivex.functions.Consumer
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.EventBusAddresses.Dao
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarId
import pt.fabm.template.models.Login
import pt.fabm.template.models.UserRegisterIn

class DaoVerticle : AbstractVerticle() {
  companion object {
    val LOGGER = LoggerFactory.getLogger(DaoVerticle::class.java)
  }

  override fun rxStart(): Completable {
    //registerCodecs()
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val cpds = ComboPooledDataSource()
    cpds.driverClass = DaoConstants.db.driver
    cpds.jdbcUrl = DaoConstants.db.url
    cpds.user = DaoConstants.db.user
    cpds.password = DaoConstants.db.password

    val unexpectedError = { message: Message<*> ->
      Consumer<Throwable> { message.fail(-1, "unexpected error") }
    }
    val eventBus = vertx.eventBus()
    val completeHandleres = mutableListOf<Completable>()

    completeHandleres += eventBus.consumer<Car>(Dao.Car.create) { message ->
      val carDao = CarDao(cpds.connection)
      carDao
        .create(message.body())
        .doOnComplete { message.reply(null) }
        .doOnError(unexpectedError(message))
        .doFinally { carDao.close() }
        .subscribeWith(DisposableLoggerCompletable(Dao.Car.create))
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<Car>(Dao.Car.update) { message ->
      val carDao = CarDao(cpds.connection)
      carDao
        .update(message.body())
        .doOnComplete { message.reply(null) }
        .doOnError(unexpectedError(message))
        .doFinally { carDao.close() }
        .subscribeWith(DisposableLoggerCompletable(Dao.Car.update))
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<Unit>(Dao.Car.list) { message ->
      val carDao = CarDao(cpds.connection)
      carDao.list()
        .toList()
        .doOnSuccess { message.reply(it, DeliveryOptions(JsonObject().put("codecName","List"))) }
        .doOnError { unexpectedError(message) }
        .doFinally { carDao.close() }
        .subscribeWith(DisposableLoggerSingle(Dao.Car.list) { it.toString() })
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<UserRegisterIn>(Dao.User.create) { message ->
      val userDao = UserDao(cpds.connection)
      userDao.create(message.body())
        .doOnComplete { message.reply(null) }
        .doFinally { userDao.close() }
        .subscribeWith(DisposableLoggerCompletable(Dao.User.create))
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<CarId>(Dao.Car.retrieve) { message ->
      val carDao = CarDao(cpds.connection)
      carDao.find(message.body())
        .doOnSuccess { message.reply(it) }
        .doOnError(unexpectedError(message))
        .doFinally { carDao.close() }
        .ignoreElement()
        .subscribeWith(DisposableLoggerCompletable(Dao.Car.retrieve))
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<CarId>(Dao.Car.delete) { message ->
      val carDao = CarDao(cpds.connection)
      carDao.remove(message.body())
        .doOnComplete { message.reply(null) }
        .doOnError(unexpectedError(message))
        .doFinally { carDao.close() }
        .subscribeWith(DisposableLoggerCompletable(Dao.Car.delete))
    }.rxCompletionHandler()

    completeHandleres += eventBus.consumer<Login>(Dao.User.login) { message ->
      val userDao = UserDao(cpds.connection)
      val login = message.body()
      userDao.getUserPass(login.username)
        .map { it.contentEquals(login.password) }
        .switchIfEmpty { observer: SingleObserver<in Boolean> -> observer.onSuccess(false) }
        .doOnSuccess { message.reply(it) }
        .doOnError(unexpectedError(message))
        .doFinally { userDao.close() }
        .subscribeWith(DisposableLoggerSingle(Dao.User.login))
    }.rxCompletionHandler()

    return Completable.merge(completeHandleres)
  }
}
