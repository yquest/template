package pt.fabm.template.rest

import io.reactivex.Completable
import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.kotlin.core.json.jsonObjectOf
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.buffer.Buffer
import io.vertx.reactivex.ext.auth.User
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.handler.StaticHandler
import pt.fabm.template.EventBusAddresses
import pt.fabm.template.extensions.*
import pt.fabm.template.models.type.Car
import pt.fabm.template.rest.controllers.CarController
import pt.fabm.template.rest.controllers.UserController
import pt.fabm.template.validation.RequiredException
import pt.fabm.tpl.component.app.CarFields
import java.io.File

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

    val userTimeout = config().getLong("user_timeout") ?: throw
    RequiredException("user cache timeout")
    val userController = UserController(vertx, userTimeout)
    val carController = CarController(vertx)

    router.get().handler {
      it.response().toObserver()
    }
    router.post("/api/user").withBody().handler(userController::createUser)
    router.post("/api/user/login").withCookies().withBody().handler(userController::userLogin)
    router.get("/api/user/logout").withCookies().handler(userController::userLogout)
    router.get("/api/car").handler(carController::getCar)
    router.get("/api/car/list").handlerSRR { carController.carList() }
    router.post("/api/car").withBody().handler(carController::createCar)
    router.put("/api/car").withBody().handler(carController::updateCar)
    router.delete("/api/car").handler(carController::dele)

    class AfterAuth(private val user: User) {
      fun renderIfLogged(rc: RoutingContext) {
        user.isAuthorized("global") { auth ->

        }
      }
    }

    router.get("/aaa").authEval(userTimeout).handler {
      AfterAuth(it.user()).renderIfLogged(it)
    }
    router.get("/").authEval(userTimeout).handler { rc ->
      fun resX(auth: Boolean) {
        if (auth) rc.response().end("auth")
        else rc.response().end("not auth")
      }

      rc.user().rxIsAuthorized("global").map(::resX).subscribe()
    }

    val renderContent = { buffer: Buffer, appInitData: Buffer ->
      val bufferWrapper = Buffer.buffer()
      bufferWrapper.appendString(
        """
        <html>
          <head>
            <meta charset="utf-8"/>
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
            <title>Car app</title>
            <link href="favicon.ico" rel="shortcut icon"/>
            <link href="main.css" rel="stylesheet"/>
          </head>
          <body>
            <div class="well">
                <div id="root">"""
      )
        .appendBuffer(buffer)
        .appendString(
          """</div>
            </div>
            <script type="text/javascript">var __state = """
        )
        .appendBuffer(appInitData)
        .appendString(
          """.trimMargin();</script>
            <script type="text/javascript" src="bundle.js"></script>
          </body>
        </html>
        """
        )
    }

    val username = "Xico"
    val edit = true
    val auth = true
    router.get("/xxxx").authHandler(userTimeout) { ac ->
      LOGGER.info("entering in root...")
      fun renderHtml(carList: List<Car>): Buffer {

        val content = Buffer.buffer().delegate.let { buffer ->

          buffer
        }

        val appInitData = jsonObjectOf(
          "page" to "init",
          "cars" to carList.map {
            CarFields(
              maker = it.make.name,
              model = it.model,
              matDate = it.maturityDate.toString(),
              price = it.price.toString()
            )
          },
          "username" to username,
          "edit" to edit,
          "auth" to auth
        )

        return renderContent(Buffer(content), Buffer(appInitData.toBuffer()))
      }

      vertx.eventBus().rxSend<List<Car>>(
        EventBusAddresses.Dao.Car.list, null, DeliveryOptions().setCodecName("List")
      ).map { message ->
        renderHtml(message.body())
      }.map {
        RestResponse(buffer = it)
      }
    }


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

