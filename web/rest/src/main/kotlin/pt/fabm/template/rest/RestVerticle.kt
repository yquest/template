package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
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
import pt.fabm.tpl.component.car.CarList
import pt.fabm.tpl.html
import java.io.File
import java.lang.StringBuilder
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class RestVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(RestVerticle::class.java)
  }

  override fun rxStart(): Completable {
    val port = config().checkedInt("port")
    val host = config().checkedString("host")

    val staticPath = config().checkedString("pdir")
    if (!File(staticPath).exists()) error("static path doesn't exists, current path is " +
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



    router.get("/index-text").handler {

      fun renderHtml(carList: List<Car>): String {

        val cars = listOf(Car(
          make = CarMake.VOLKSWAGEN,
          maturityDate = Instant.ofEpochMilli(1567594959104L),
          model = "golf v",
          price = 3_000
        ),Car(
          make = CarMake.AUDI,
          maturityDate = Instant.ofEpochMilli(1567594959104L),
          model = "A6",
          price = 30_000
        ))
        var content = StringBuilder().let { CarList(Type.SERVER,false,cars).create().renderTag(it);it.toString()}
        val html = """
        <html>
          <head>
            <link href="favicon.ico" rel="shortcut icon">
            <link href="main.css" rel="stylesheet">
          </head>
          <body>
            <div class="well">
                <div id="root">${content}</div>
            </div>
            <script >
              var initialRequest = [
                {"make":"VOLKSWAGEN","maturityDate":1567594959104,"model":"golf v","price":3000},
                {"make":"AUDI","maturityDate":1567594959104,"model":"A6","price":30000},
              ];
            </script>
            <script type="text/javascript" src="bundle.js?a41"></script>
          </body>
        </html>
        """.trimIndent()

        return html
      }
      val response = it.response()
      vertx.eventBus().rxSend<List<Car>>(
        EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
      ).map { message ->
        response.end(renderHtml(message.body()))
      }.subscribe()
    }
    router.route().handler {
      if (!it.response().ended()) {
        it.response()
        LOGGER.error("Attention, not ended route for url: ${it.normalisedPath()}")
        it.response().end()
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

