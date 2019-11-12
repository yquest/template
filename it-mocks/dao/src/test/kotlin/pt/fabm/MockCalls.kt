package pt.fabm

import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.web.client.HttpResponse
import pt.fabm.template.models.type.CarMake
import java.time.Instant

val conf = Conf()


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

  //loginXico.subscribe(onDone,onError)
  val buffer =Buffer.buffer()
  buffer.appendString("enum CarMaker{")
  val iterator = CarMake.values().iterator()
  buffer.appendString(iterator.next().name)
  iterator.forEachRemaining { buffer.appendString(", ${it.name}") }
  buffer.appendString("}")

  conf.vertx.fileSystem().writeFile("x.txt",buffer,{
    println("printed")
  })
}
