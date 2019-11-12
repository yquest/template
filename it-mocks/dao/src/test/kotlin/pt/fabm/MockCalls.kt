package pt.fabm

import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpResponse
import pt.fabm.template.models.type.CarMake
import java.time.Instant

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


fun getAccessTokenCookie(response: HttpResponse<Buffer>) =
  response.cookies().find { it.startsWith("access_token") }.toString()

fun listUsers(response: HttpResponse<Buffer>) {
  println(response.bodyAsJsonArray().encodePrettily())
}

fun main() {

  val onError: (Throwable) -> Unit = { it.printStackTrace() }

  val createUserXico = conf.client.createUser {
    name = "xico"
    pass = "1234"
    email = "xiko@gmail.com"
  }

  val loginXico = conf.client.login {
    name = "xico"
    pass = "1234"
  }

  val createCreateGolf4: (String) -> Single<HttpResponse<Buffer>> = {
    conf.client.createCar(it) {
      matDate = Instant.now()
      price = 1234
      model = "Golf 4"
      maker = CarMake.VOLKSWAGEN
    }
  }

  val onDone:(String)->Unit ={
    println("cookie:$it")
  }

  loginXico.subscribe(onDone,onError)

}
