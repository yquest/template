package pt.fabm.template.dao

import com.mchange.v2.c3p0.ComboPooledDataSource
import io.reactivex.Completable
import io.vertx.reactivex.core.AbstractVerticle
import pt.fabm.template.EventBusAddresses.Dao

class DaoVerticle : AbstractVerticle() {

  override fun rxStart(): Completable {
    return createMessageConsumers()
  }

  private fun createMessageConsumers(): Completable {
    val cpds = ComboPooledDataSource()
    cpds.driverClass = DaoConstants.db.driver
    cpds.jdbcUrl = DaoConstants.db.url
    cpds.user = DaoConstants.db.user
    cpds.password = DaoConstants.db.password

    val eventBus = vertx.eventBus()

    val carDaoSupplier = { CarDaoPostgres(cpds.connection) }
    val userDaoSupplier = { UserDaoPostgres(cpds.connection) }

    return Completable.mergeArray(
      eventBus.execCloseableDao(Dao.Car.create, carDaoSupplier, CarDaoPostgres::create),
      eventBus.execCloseableDao(Dao.Car.update, carDaoSupplier, CarDaoPostgres::update),
      eventBus.execCloseableDao(Dao.Car.list, carDaoSupplier, CarDaoPostgres::list),
      eventBus.execCloseableDao(Dao.Car.delete, carDaoSupplier, CarDaoPostgres::delete),
      eventBus.execCloseableDao(Dao.Car.retrieve, carDaoSupplier, CarDaoPostgres::find),
      eventBus.execCloseableDao(Dao.User.create, userDaoSupplier, UserDaoPostgres::create),
      eventBus.execCloseableDao(Dao.User.login, userDaoSupplier, UserDaoPostgres::login)
    )
  }
}
