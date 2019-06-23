package pt.fabm.template.dao

import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

class DisposableLoggerMaybe<T>(private val element:String, private val toString:(T)->String={""}):DisposableMaybeObserver<T>() {
  override fun onComplete() {
    LOGGER.debug("rx complete {0}",element)
  }

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
