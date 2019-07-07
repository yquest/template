package pt.fabm.template.dao

import io.reactivex.Completable
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.eventbus.EventBus
import io.vertx.reactivex.core.eventbus.Message
import java.io.Closeable

val LOGGER = LoggerFactory.getLogger(Closeable::class.java)!!

fun <T, D : Closeable> EventBus.execCloseableDao(
  address: String,
  daoCreator: () -> D,
  execution: (D, Message<T>) -> Unit
): Completable = this.consumer<T>(address) { message ->
  var dao: D? = null
  try {
    dao = daoCreator()
    execution(dao, message)
  } catch (e: Exception) {
    LOGGER.error("unexpected error", e)
    message.fail(0, "unexpected error")
  } finally {
    dao?.close()
  }
}.rxCompletionHandler()

