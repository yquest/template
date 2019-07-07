package pt.fabm.template.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import io.reactivex.Completable
import io.vertx.core.logging.LoggerFactory
import io.vertx.reactivex.core.AbstractVerticle
import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.EventBusAddresses.Dao
import java.io.Closeable

class DaoVerticle : AbstractVerticle() {
  companion object {
    val LOGGER = LoggerFactory.getLogger(DaoVerticle::class.java)!!
  }

  override fun rxStart(): Completable {
    //registerCodecs()
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val cpds = ComboPooledDataSource()
    cpds.driverClass = DaoConstants.db.driver
    cpds.jdbcUrl = DaoConstants.db.url
    cpds.user = DaoConstants.db.user
    cpds.password = DaoConstants.db.password

    val eventBus = vertx.eventBus()

    fun <T, D : Closeable> daoExecution(
      address: String,
      daoCreation: () -> D,
      execution: (D, Message<T>) -> Unit
    ): Completable = eventBus.consumer<T>(address) { message ->
      var dao: D? = null
      try {
        dao = daoCreation()
        execution(dao, message)
      } catch (e: Exception) {
        LOGGER.error("unexpected error", e)
        message.fail(0, "unexpected error")
      } finally {
        dao?.close()
      }
    }.rxCompletionHandler()

    val carDaoSupplier = { CarDaoPostgres(cpds.connection) }
    val userDaoSupplier = { UserDaoPostgres(cpds.connection) }

    return Completable.mergeArray(
      daoExecution(Dao.Car.create, carDaoSupplier, CarDaoPostgres::create),
      daoExecution(Dao.Car.update, carDaoSupplier, CarDaoPostgres::update),
      daoExecution(Dao.Car.list, carDaoSupplier, CarDaoPostgres::list),
      daoExecution(Dao.User.create, userDaoSupplier, UserDaoPostgres::create),
      daoExecution(Dao.User.login, userDaoSupplier, UserDaoPostgres::login)
    )
  }
}
