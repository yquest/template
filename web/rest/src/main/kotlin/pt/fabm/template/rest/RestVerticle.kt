package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.web.Router
import pt.fabm.template.extensions.*
import pt.fabm.template.rest.controllers.CarController
import pt.fabm.template.rest.controllers.UserController

class RestVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(RestVerticle::class.java)
  }

  override fun rxStart(): Completable {
    val port = config().checkedInt("port")
    val host = config().checkedString("host")

    val router = Router.router(vertx)
    val webRoot = StaticHandlerImpl(null,null)
      .setAllowRootFileSystemAccess(true)
      .setWebRoot(Consts.PUBLIC_DIR)
      .skipCompressionForMediaTypes(setOf("font/woff2"))


    router.route().handler{
      webRoot.handle(it.delegate)
    }


    val userService = UserController(vertx)
    val carController = CarController(vertx)


    //trace
    router.route().handler { rc->
      LOGGER.info("start:${rc.normalisedPath()}")
      rc.addBodyEndHandler{
        LOGGER.info("end:${rc.normalisedPath()}")
      }
      rc.next()
    }

    router.post("/api/user").withBody().handlerSRR(userService::createUser)
    router.post("/api/user/login").withCookies().withBody().handlerSRR(userService::userLogin)
    router.get("/api/user/logout").withCookies().handlerSRR(userService::userLogout)
    router.get("/api/car").handlerSRR(carController::getCar)
    router.get("/api/car/list").handlerSRR { carController.carList() }
    router.post("/api/car").withBody().authHandler { carController.createOrUpdateCar(true,it.rc) }
    router.put("/api/car").withBody().authHandler { carController.createOrUpdateCar(false,it.rc) }
    router.delete("/api/car").handlerSRR(carController::deleteCar)

    router.route().handler {
      if (!it.response().ended()) {
        LOGGER.error("Attention, not ended route")
        it.response().end()
      } else {
        LOGGER.info("end:${it.normalisedPath()}")
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
      .doOnSuccess{
        LOGGER.info("connected in host:$host and port:$port")
      }
      .doOnError { LOGGER.error("Http server initialization error!", it) }
      .ignoreElement()
  }

}

