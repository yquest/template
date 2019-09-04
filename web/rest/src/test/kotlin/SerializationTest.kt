import io.vertx.kotlin.core.json.jsonObjectOf
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import pt.fabm.template.extensions.toJson
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import java.time.LocalDateTime
import java.time.ZoneOffset

class SerializationTest {
  @Test
  fun serializeCar() {
    val car = Car(
      "Golf V",
      CarMake.VOLKSWAGEN,
      30000,
      LocalDateTime.of(2019, 1, 2, 3, 4).toInstant(ZoneOffset.UTC)
    )
    Assertions.assertEquals(
        jsonObjectOf(
        "model" to "Golf V",
        "make" to "VOLKSWAGEN",
        "price" to 30000,
        "maturityDate" to 1546398240000
      ), car.toJson()
    )
  }
}
