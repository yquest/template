package pt.fabm

import io.vertx.junit5.VertxExtension
import io.vertx.junit5.VertxTestContext
import io.vertx.reactivex.core.Vertx
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.LocalCodec
import pt.fabm.template.dao.DaoVerticle
import pt.fabm.template.models.*
import java.time.LocalDateTime

@ExtendWith(VertxExtension::class)
class DaoVerticleTest {

  @BeforeEach
  fun deployVerticle(vertx: Vertx, testContext: VertxTestContext) {
    fun <T> registerLocalCodec(klass: Class<T>) {
      vertx.eventBus().delegate.registerDefaultCodec(klass, LocalCodec(klass))
    }

    fun registerCodecs() {
      registerLocalCodec(UserRegisterIn::class.java)
      registerLocalCodec(SimpleDate::class.java)
      registerLocalCodec(Reservation::class.java)
      registerLocalCodec(Car::class.java)
      registerLocalCodec(CarId::class.java)
      registerLocalCodec(java.util.List::class.java)
    }

    registerCodecs()

    vertx.rxDeployVerticle(DaoVerticle::class.java.name)
      .doOnSuccess { testContext.completeNow() }
      .doOnError(testContext::failNow)
      .subscribe()
  }

  @Test
  fun testCreateCar(vertx: Vertx, testContext: VertxTestContext) {

    vertx.eventBus().rxSend<Unit>(
      EventBusAddresses.Dao.Car.delete, CarId(
        maker = CarMake.VOLKSWAGEN,
        model = "model"
      )
    ).ignoreElement().andThen(
      vertx.eventBus().rxSend<Unit>(
        EventBusAddresses.Dao.Car.create, Car(
          model = "model",
          make = CarMake.VOLKSWAGEN,
          maturityDate = LocalDateTime.now(),
          price = 200
        )
      )
    ).doOnSuccess { testContext.completeNow() }
      .doOnError(testContext::failNow)
      .subscribe()
  }

  @Test
  fun testUpdateCar(vertx: Vertx, testContext: VertxTestContext) {
    val car1 = Car(
      model = "model",
      make = CarMake.VOLKSWAGEN,
      maturityDate = LocalDateTime.now(),
      price = 200
    )
    val car2 = Car(
      model = "model",
      make = CarMake.VOLKSWAGEN,
      maturityDate = LocalDateTime.now(),
      price = 201
    )
    vertx.eventBus().rxSend<Unit>(
      EventBusAddresses.Dao.Car.delete, CarId(
        maker = CarMake.VOLKSWAGEN,
        model = "model"
      )
    ).ignoreElement().andThen(
      vertx.eventBus().rxSend<Unit>(
        EventBusAddresses.Dao.Car.create, car1
      ).ignoreElement().andThen(
        vertx.eventBus().rxSend<Unit>(EventBusAddresses.Dao.Car.update, car2)
      )
    ).doOnSuccess { testContext.completeNow() }
      .doOnError(testContext::failNow)
      .subscribe()
  }
}
