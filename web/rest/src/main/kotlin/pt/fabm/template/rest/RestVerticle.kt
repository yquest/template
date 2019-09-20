package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.StaticHandler
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.*
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarMake
import pt.fabm.template.rest.controllers.CarController
import pt.fabm.template.rest.controllers.UserController
import pt.fabm.template.validation.RequiredException
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.page.App
import pt.fabm.tpl.component.page.Page2
import java.io.File
import java.time.Instant

class RestVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(RestVerticle::class.java)
  }

  override fun rxStart(): Completable {
    val port = config().checkedInt("port")
    val host = config().checkedString("host")

    val staticPath = config().checkedString("pdir")
    if (!File(staticPath).exists()) error(
      "static path doesn't exists, current path is " +
        File(".").canonicalPath
    )

    val router = Router.router(vertx)
    val webRoot = StaticHandler
      .create()
      .setAllowRootFileSystemAccess(true)
      .setWebRoot(staticPath)

    router.route().handler(webRoot)

    val userTimeout = config().getLong("user_timeout") ?: throw
    RequiredException("user cache timeout")
    val userController = UserController(vertx, userTimeout)
    val carController = CarController(vertx)

    router.post("/api/user").withBody().handlerSRR(userController::createUser)
    router.post("/api/user/login").withCookies().withBody().handlerSRR(userController::userLogin)
    router.get("/api/user/logout").withCookies().handlerSRR(userController::userLogout)
    router.get("/api/car").handlerSRR(carController::getCar)
    router.get("/api/car/list").handlerSRR { carController.carList() }
    router.post("/api/car").withBody().authHandler(userTimeout) { carController.createOrUpdateCar(true, it.rc) }
    router.put("/api/car").withBody().authHandler(userTimeout) { carController.createOrUpdateCar(false, it.rc) }
    router.delete("/api/car").handlerSRR(carController::deleteCar)

    val renderContent = { content: String, appInitData: JsonObject ->
      """
        <html>
          <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
            <title>Car app</title>
            <link href="favicon.ico" rel="shortcut icon">
            <link href="main.css" rel="stylesheet">
          </head>
          <body>
            <div class="well">
                <div id="root">${content}</div>
            </div>
            <script type="text/javascript">var __state = $appInitData;</script>
            <script type="text/javascript" src="bundle.js?2"></script>
          </body>
        </html>
        """.trimIndent()
    }

    val username = "Xico"
    val edit = true
    val auth = true
    router.get("/").handler {

      fun renderHtml(carList: List<Car>): String {

        val cars = listOf(
          Car(
            make = CarMake.VOLKSWAGEN,
            maturityDate = Instant.ofEpochMilli(1567594959104L),
            model = "golf v",
            price = 3_000
          ), Car(
            make = CarMake.AUDI,
            maturityDate = Instant.ofEpochMilli(1567594959104L),
            model = "A6",
            price = 30_000
          ), Car(
            make = CarMake.PEUGEOT,
            maturityDate = Instant.ofEpochMilli(1567594959104L),
            model = "308",
            price = 30_000
          )
        )


        var content = StringBuilder().let {
          App(
            type = Type.SERVER,
            carList = cars,
            username = { username },
            carEdit = edit,
            auth = auth
          ).create().renderTag(it);it.toString()
        }

        val appInitData = jsonObjectOf(
          "page" to "init",
          "cars" to cars.map { car -> car.toJson() },
          "username" to username,
          "edit" to edit,
          "auth" to auth
        )

        return renderContent(content, appInitData)
      }


      val response = it.response()
      vertx.eventBus().rxSend<List<Car>>(
        EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
      ).map { message ->
        response.end(renderHtml(message.body()))
      }.subscribe()
    }

    router.get("/page2").handler { rc ->
      val input1 = "myTest 1"
      val input2 = "myTest 2"
      val appInitData = jsonObjectOf(
        "page" to "page2",
        "username" to username,
        "edit" to edit,
        "auth" to auth,
        "input1" to input1,
        "input2" to input2
      )

      rc.response().end(Page2(
        type = Type.SERVER,
        edit = edit,
        auth = auth,
        username = { username },
        input1 = { input1 },
        input2 = { input2 }
      ).create().let {
        val sb = StringBuilder()
        it.renderTag(sb)
        renderContent(sb.toString(), appInitData)
      })
    }

    router.route().handler {
      if (!it.response().ended()) {
        it.response().statusCode = 404
        it.response().end("ressource not found")
      }
    }

    router.route().failureHandler {
      LOGGER.error("failure", it.failure())
      it.response().end("oh no!")
    }

    return vertx
      .createHttpServer()
      .requestHandler(router)
      .rxListen(port, host)
      .doOnSuccess {
        LOGGER.info("connected in host:$host and port:$port")
      }
      .doOnError { LOGGER.error("Http server initialization error!", it) }
      .ignoreElement()
  }

}

