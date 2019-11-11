package pt.fabm

import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpResponse
import pt.fabm.template.models.type.CarMake
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

val conf = Conf()

fun carCreated(response: HttpResponse<Buffer>) {
  println("car created (status code=${response.statusCode()})")
}

fun authenticated(response: HttpResponse<Buffer>) {
  println("authenticated user (status code=${response.statusCode()})")

  val authCookie = response.cookies().find { it.startsWith("access_token") }.toString()
  conf.client.createCar(authCookie) {
    maker = CarMake.NISSAN
    matDate = Instant.now()
    model = "Ferrari"
    price = 20000
  }.subscribe(::carCreated)
}

fun createdUser(response: HttpResponse<Buffer>) {
  println("created user (status code=${response.statusCode()})")
  conf.client.login {
    name = "xico"
    pass = "my***pass"
  }.subscribe(::authenticated)
}

fun getAccessTokenCookie(response: HttpResponse<Buffer>) =
  response.cookies().find { it.startsWith("access_token") }.toString()

fun listCars(response: HttpResponse<Buffer>) {
  println(response.bodyAsJsonArray().encodePrettily())
}

fun main() {

  val createCar: (HttpResponse<Buffer>) -> Unit = {
    val token = getAccessTokenCookie(it)
    conf.client.createCar(token) {
      maker = CarMake.PEUGEOT
      model = "206"
      price = 30_000
      matDate = LocalDate.of(2019, 1, 1)
        .atTime(20, 20)
        .toInstant(ZoneOffset.UTC)
    }.subscribe()
  }

  conf.client.listCars().subscribe(::listCars)

}
