package pt.fabm

import io.reactivex.Single
import io.vertx.core.cli.Argument
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.term.HttpTermOptions
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.core.cli.CLI
import io.vertx.reactivex.ext.shell.ShellService
import io.vertx.reactivex.ext.shell.command.CommandBuilder
import io.vertx.reactivex.ext.shell.command.CommandRegistry
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

  val registry = CommandRegistry.getShared(conf.vertx)

  val cli = CLI.create("mock-user").addArgument(Argument().setArgName("action"))
  val command = CommandBuilder.command(cli)
  command.completionHandler { completion ->
    val possibilities = listOf("clean", "insert", "delete")
    val lastToken = completion.lineTokens().last().value()

    val candidates = possibilities.filter { it.startsWith(lastToken) }.let {
      if (it.isEmpty()) possibilities else it
    }

    if (completion.lineTokens().size < 3) {
      if (candidates.size == 1) completion.complete(candidates.get(0).substring(lastToken.length), true)
      else completion.complete(candidates)
    }else{
      completion.complete(emptyList())
    }
  }
  command.processHandler { process ->

    val commandLine = process.commandLine()
    val arg:String = commandLine.getArgumentValue(0)
    println(""" value:$arg """)
    process.end()
  }
  registry.registerCommand(command.build(conf.vertx))

  //open in
  ShellService.create(
    conf.vertx, ShellServiceOptions()
      .setHttpOptions(
        HttpTermOptions()
          .setHost("localhost")
          .setPort(8123)
      )
  ).rxStart()
    .subscribe({ println("shell started") }, onError)

}
