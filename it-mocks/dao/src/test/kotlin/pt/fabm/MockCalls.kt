package pt.fabm

import com.datastax.driver.core.SimpleStatement
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.term.HttpTermOptions
import io.vertx.ext.shell.term.TelnetTermOptions
import io.vertx.reactivex.cassandra.CassandraClient
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.shell.ShellService
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

  val onDone: (String) -> Unit = {
    println("cookie:$it")
  }

  ShellTest(conf.vertx)

  //open in http://localhost:8123/shell.html
  ShellService.create(
    conf.vertx, ShellServiceOptions()
      .setTelnetOptions(
        TelnetTermOptions()
          .setHost("localhost")
          .setPort(2222)
      )
      .setHttpOptions(
        HttpTermOptions()
          .setHost("localhost")
          .setPort(8123)
      ).setWelcomeMessage("hi there!\n")
  ).rxStart()
    .subscribe({ println("shell started") }, onError)

}
