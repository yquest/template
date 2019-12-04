package pt.fabm

import io.vertx.config.ConfigRetrieverOptions
import io.vertx.config.ConfigStoreOptions
import io.vertx.core.Handler
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.config.ConfigRetriever
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.cli.CLI
import io.vertx.reactivex.ext.shell.command.CommandBuilder
import io.vertx.reactivex.ext.shell.command.CommandProcess
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
        command("login") { cmd ->
          currentUser.flatMap { conf.client.login(it) }
            .subscribe({
              conf.token = it
              cmd.write("token:${it}\n")
              cmd.end()
            }, {
              cmd.write("error on login")
              cmd.end()
            })
        }
      }
      node("car") {
        command("create") { cmd ->
          currentCar.flatMap { carEntry ->
            conf.client.createCar(conf.token ?: error("no token"), carEntry)
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
            cmd.write("error on list cars\n")
            cmd.end()
          })
        }
      }
      node("view") {
        command("main") { cmd ->
          conf.client.mainPage(conf.token).subscribe({
            cmd.write(it.bodyAsString())
            cmd.end()
          }, {
            it.printStackTrace()
            cmd.write("error on render main page")
            cmd.end()
          })
        }
      }

    }.let { registry.registerCommand(it.build(vertx)) }

    fun createCommand(
      name: String,
      cmdProcessHandler: Handler<CommandProcess>
    ) {
      val cli = CLI.create(name)
      val cmdBuilder = CommandBuilder.command(cli)
      cmdBuilder.processHandler(cmdProcessHandler)
      registry.registerCommand(cmdBuilder.build(vertx))
    }

    createCommand("cql", Handler { cmd ->
      val cqlCommand = cmd.args().joinToString(" ")
      println("executing:${cqlCommand}")
      conf.cassandraLocalClient.executeString(cqlCommand).subscribe({ rs ->
        rs.all {
          if (it.succeeded()) {
            for (row in it.result()) {
              cmd.write(row.toString())
              cmd.write("\n")
            }
          } else {
            cmd.write("error")
          }
        }
        cmd.end()
      }, {
        it.printStackTrace()
        cmd.end()
      })
    })

    createCommand("cql-metadata", Handler { cmd ->

      val cqlCommand = if (cmd.args().isEmpty()) {
        "select keyspace_name, table_name from system_schema.tables;"
      } else if (cmd.args().size == 1) {
        "select table_name, column_name,type from system_schema.columns where keyspace_name ='${cmd.args()[0]}';"
      } else {
        "select column_name,type from system_schema.columns where keyspace_name ='${cmd.args()[0]}' AND table_name='${cmd.args()[1]}';"
      }

      println("executing:${cqlCommand}")
      conf.cassandraLocalClient.executeString(cqlCommand).subscribe({ rs ->
        rs.all {
          if (it.succeeded()) {
            for (row in it.result()) {
              cmd.write(row.toString())
              cmd.write("\n")
            }
          } else {
            cmd.write("error")
          }
        }
        cmd.end()
      }, {
        it.printStackTrace()
        cmd.end()
      })

    })

    createCommand("cql-types", Handler { cmd ->

      val cqlCommand = "select * from system_schema.types;"

      println("executing:${cqlCommand}")
      conf.cassandraLocalClient.executeString(cqlCommand).subscribe({ rs ->
        rs.all {
          if (it.succeeded()) {
            for (row in it.result()) {
              cmd.write(row.toString())
              cmd.write("\n")
            }
          } else {
            cmd.write("error")
          }
        }
        cmd.end()
      }, {
        it.printStackTrace()
        cmd.end()
      })

    })

  }
}
