package pt.fabm

import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.shell.command.CommandRegistry
import pt.fabm.shell.LevelArgument
import pt.fabm.template.models.type.CarMake
import java.text.SimpleDateFormat

class ShellTest(private val vertx: Vertx) {
  init {

    val store = ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(
        JsonObject()
          .put("path", "test-entries.yaml")
      )
    val retriever = ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(store))
    val currentCar = retriever
      .rxGetConfig().map { it.getJsonObject("car") }.map { jo ->
        CarEntry(
          model = jo.getString("model"),
          maker = CarMake.valueOf(jo.getString("maker")),
          price = jo.getInteger("price"),
          matDate = jo.getString("date").let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
              .parse(it)
              .toInstant()
          }
        )
      }

    val currentUser = retriever
      .rxGetConfig().map { it.getJsonObject("user") }.map { jo ->
        User(
          name = jo.getString("name"),
          pass = jo.getString("pass"),
          email = jo.getString("email")
        )
      }

    val registry = CommandRegistry.getShared(vertx)
    LevelArgument.root("test") {
      node("user") {
        command("create") { cmd ->
          currentUser
            .flatMap(conf.client::createUser)
            .subscribe({
              cmd.write("created user successfully\n")
              cmd.end()
            }, {
              cmd.write("error on create user\n")
              it.printStackTrace()
              cmd.end()
            })
        }
        command("list") { cmd ->
          conf.clientMock.listUsers().subscribe({
            cmd.write(it.encodePrettily() + "\n")
            cmd.end()
          }, {
            cmd.write("error on list users")
            cmd.end()
          })
        }
      }
      node("car") {
        command("create") { cmd ->
          currentUser.flatMap(conf.client::login)
            .flatMap { token ->
              currentCar.flatMap { carEntry ->
                conf.client.createCar(token, carEntry)
              }
            }.subscribe({
              cmd.write("car created successfully\n")
              cmd.end()
            }, {
              it.printStackTrace()
              cmd.write("error on create car")
              cmd.end()
            })
        }
        command("list") { cmd ->
          conf.client.listCars().subscribe({
            cmd.write(it.encodePrettily() + "\n")
            cmd.end()
          }, {
            it.printStackTrace()
            cmd.write("error on list cars")
            cmd.end()
          })
        }
      }
    }.let { registry.registerCommand(it.build(vertx)) }
  }
}
