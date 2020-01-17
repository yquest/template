package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.ext.auth.User
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.StaticHandler
import pt.fabm.template.extensions.withBody
import pt.fabm.template.extensions.withCookies
import pt.fabm.template.rest.controllers.CarController
import pt.fabm.template.rest.controllers.UserController
import pt.fabm.template.rest.controllers.ViewsController
import pt.fabm.template.validation.RequiredException
import java.io.File

class RestVerticle : AbstractVerticle() {

  companion object {
    val LOGGER: Logger = LoggerFactory.getLogger(RestVerticle::class.java)
    const val PORT = "port"
    const val HOST = "host"
    const val PDIR = "pdir"

  }

  override fun rxStart(): Completable {
    val port = config().getInteger(PORT) ?: throw RequiredException(PORT)
    val host = config().getString(HOST) ?: throw RequiredException(HOST)

    val staticPath = config().getString(PDIR) ?: throw RequiredException(PDIR)
    if (!File(staticPath).exists()) error(
      "static path doesn't exists, current path is " +
        File(".").canonicalPath
    )

    val router = Router.router(vertx)

    val userTimeout = config().getLong("user_timeout") ?: throw
    RequiredException("user cache timeout")
    val userController = UserController(vertx, userTimeout)
    val carController = CarController(vertx)
    val viewsController = ViewsController(vertx)

    router.post("/api/user").withBody().handler(userController::createUser)
    router.post("/api/user/login").withCookies().withBody().handler(userController::userLogin)
    router.get("/api/user/logout").withCookies().handler(userController::userLogout)
    router.get("/api/car").handler(carController::getCar)
    router.get("/api/car/list").handler(carController::carList)
    router.post("/api/car").withBody().handler(carController::createCar)
    router.put("/api/car").withBody().handler(carController::updateCar)
    router.delete("/api/car").handler(carController::deleteCar)
    router.get("/").handler(viewsController::main)
    router.get("/login").handler(viewsController::login)

    val webRoot = StaticHandler
      .create()
      .setAllowRootFileSystemAccess(true)
      .setWebRoot(staticPath)

    router.route().handler(webRoot)

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

