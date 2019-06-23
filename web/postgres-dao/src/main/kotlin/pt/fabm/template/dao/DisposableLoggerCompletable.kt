package pt.fabm.template.dao

import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableSingleObserver
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

class DisposableLoggerCompletable(private val element:String):DisposableCompletableObserver() {

  companion object{
    private val LOGGER: Logger = LoggerFactory.getLogger(DisposableSingleObserver::class.java)
  }
  override fun onError(e: Throwable) {
    LOGGER.error("rx error ${element}",e)
  }

  override fun onComplete() {
    LOGGER.debug("rx {0}",element)
  }
}
