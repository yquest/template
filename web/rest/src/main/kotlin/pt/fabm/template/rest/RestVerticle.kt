package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.handler.StaticHandler
import pt.fabm.template.extensions.*
import pt.fabm.template.rest.controllers.CarController
import pt.fabm.template.rest.controllers.UserController
import pt.fabm.template.validation.RequiredException
import pt.fabm.tpl.Type
import pt.fabm.tpl.component.car.CarList
import pt.fabm.tpl.html
import java.lang.StringBuilder

class RestVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(RestVerticle::class.java)
  }

  override fun rxStart(): Completable {
    val port = config().checkedInt("port")
    val host = config().checkedString("host")

    val router = Router.router(vertx)
    val webRoot = StaticHandler
      .create()
      .setAllowRootFileSystemAccess(true)
      .setWebRoot(config().checkedString("pdir"))

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

    router.get("/test-index").handler {
      val builder = StringBuilder()
      html(Type.SERVER) {
        head {
          link(rel = "shortcut icon", href = "favicon.ico")
          link(href = "main.css", rel = "stylesheet")
        }

        body {
          div(id = "root") {
            children += CarList(type,false, listOf(

            ))
          }
          scriptJS("bundle.js")
        }
      }.create().renderTag(builder)
      it.response().end(builder.toString())
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

