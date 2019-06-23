package pt.fabm.template.dao

import io.reactivex.observers.DisposableSingleObserver
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

class DisposableLoggerSingle<T>(private val element:String, private val toString:(T)->String={""}):DisposableSingleObserver<T>() {
  companion object{
    private val LOGGER:Logger = LoggerFactory.getLogger(DisposableSingleObserver::class.java)
  }
  override fun onError(e: Throwable) {
    LOGGER.error("rx error ${element}",e)
  }

  override fun onSuccess(t: T) {
    LOGGER.debug("rx {0} {1}",element,toString(t))
  }
}
