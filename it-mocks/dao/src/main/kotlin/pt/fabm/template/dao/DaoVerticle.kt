package pt.fabm.template.dao

import io.reactivex.Completable
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.BodyHandler
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.models.type.*
import java.time.Instant

class DaoVerticle : AbstractVerticle() {

  override fun rxStart(): Completable {
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    LOGGER.info("createMessageConsumers")
    val eventBus = vertx.eventBus()
    val userDao = UserDaoMemory()
    val carDao = CarDaoMemory()

    val port = config().getInteger("port") ?: error("port required")
    val host = config().getString("host") ?: error("host required")

    val router = Router.router(vertx)
    router.post("/event-bus").handler(BodyHandler.create()).handler { handler ->
      val ebMessage = handler.bodyAsJson
      val address = ebMessage.getString("address")
      val getBody = { ebMessage.getJsonObject("body") }

      val getLogin = {
        val body = getBody()
        Login(
          username = body.getString(UserRegisterIn.NAME),
          password = body.getString(UserRegisterIn.PASS).toByteArray()
        )
      }

      val getUserRegisterIn = {
        val body = getBody()
        UserRegisterIn(
          name = body.getString(UserRegisterIn.NAME),
          pass = body.getString(UserRegisterIn.PASS).toByteArray(),
          email = body.getString(UserRegisterIn.EMAIL)
        )
      }

      val userRegisterToJson: (UserRegisterIn) -> JsonObject = {
        JsonObject()
          .put(UserRegisterIn.NAME, it.name)
          .put(UserRegisterIn.PASS, it.pass)
          .put(UserRegisterIn.EMAIL, it.email)
      }

      val carToJson: (Car) -> JsonObject = {
        JsonObject()
          .put(Car.MAKE, it.make.ordinal)
          .put(Car.PRICE, it.price)
          .put(Car.MATURITY_DATE, it.maturityDate.epochSecond)
          .put(Car.MODEL, it.model)
      }

      val getCar: () -> Car = {
        val body = getBody()
        Car(
          make = CarMake.values()[body.getInteger(Car.MAKE)],
          price = body.getInteger(Car.PRICE),
          maturityDate = Instant.ofEpochMilli(body.getLong(Car.MATURITY_DATE)),
          model = body.getString(Car.MODEL)
        )
      }

      val getCarId: () -> CarId = {
        val body = getBody()
        CarId(
          model = body.getString(Car.MODEL),
          maker = CarMake.values()[body.getInteger(Car.MAKE)]
        )
      }

      val errorHandler = { error: Throwable ->
        LOGGER.error(error)
        handler.response().end(error.message)
      }


      when (address) {
        USER_LIST -> {
          val content = userDao.users.values
            .map(userRegisterToJson)
            .let { JsonArray(it) }
            .encode()
          handler.response().end(content)
        }
        CAR_TRUNCATE -> {
          carDao.cars.clear()
          handler.response().end()
        }
        USER_TRUNCATE -> {
          userDao.users.clear()
          handler.response().end()
        }
        EventBusAddresses.Dao.User.create -> {
          eventBus.rxSend<Unit>(address, getUserRegisterIn()).subscribe({
            handler.response().end()
          }, errorHandler)
        }
        EventBusAddresses.Dao.User.login -> {
          eventBus.rxSend<Boolean>(address, getLogin()).subscribe({
            handler.response().end("login:${it.body()}")
          }, errorHandler)
        }
        EventBusAddresses.Dao.Car.list -> {
          eventBus.rxSend<List<Car>>(address, null).subscribe({
            val content = it.body().map(carToJson).let { JsonArray(it) }.encode()
            handler.response().end(content)
          }, errorHandler)
        }
        EventBusAddresses.Dao.Car.create -> {
          eventBus.rxSend<Unit>(address, getCar()).subscribe({
            handler.response().end()
          }, errorHandler)
        }
        EventBusAddresses.Dao.Car.update -> {
          eventBus.rxSend<Unit>(address, getCar()).subscribe({
            handler.response().end()
          }, errorHandler)
        }
        EventBusAddresses.Dao.Car.retrieve -> {
          eventBus.rxSend<Unit>(address, getCarId()).subscribe({
            handler.response().end()
          }, errorHandler)
        }
        EventBusAddresses.Dao.Car.delete -> {
          eventBus.rxSend<Unit>(address, getCarId()).doOnSuccess {
            handler.response().end()
          }.subscribe()
        }
        else -> {
          handler.response().end("no address defined with:'$address'")
        }
      }
    }

    val mockServer = vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(port, host)
      .doOnSuccess {
        LOGGER.info("mock connected in host:$host and port:$port")
      }
      .doOnError { LOGGER.error("Http server initialization error!", it) }
      .ignoreElement()

    DaoMemoryShared.cars = carDao.cars
    DaoMemoryShared.users = userDao.users

    return Completable.mergeArray(
      eventBus.consumer(EventBusAddresses.Dao.Car.create, carDao::create).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.update, carDao::update).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.list, carDao::list).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.retrieve, carDao::find).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.Car.delete, carDao::delete).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.User.create, userDao::create).rxCompletionHandler(),
      eventBus.consumer(EventBusAddresses.Dao.User.login, userDao::login).rxCompletionHandler(),
      mockServer
    )
  }

  companion object {
    public const val USER_LIST = "dao.user.list"
    public const val USER_TRUNCATE = "dao.user.truncate"
    public const val CAR_TRUNCATE = "dao.car.truncate"
  }
}
