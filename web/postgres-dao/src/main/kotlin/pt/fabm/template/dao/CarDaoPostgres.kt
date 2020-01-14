package pt.fabm.template.dao

import io.vertx.core.eventbus.DeliveryOptions
import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.type.Car
import pt.fabm.template.models.type.CarId
import pt.fabm.template.models.type.CarMake
import java.io.Closeable
import java.sql.Connection
import java.sql.ResultSet
import java.sql.Timestamp

class CarDaoPostgres(private val connection: Connection) : Closeable, CarDao {

  companion object {
    private const val INSERT_QUERY = "insert into car(maker,maturity_date,model,price) values (?,?,?,?)"
    private const val UPDATE_QUERY = "update car set maturity_date = ?, price = ? where maker = ? and model = ?"
    private const val DELETE_QUERY = "delete from car where maker = ? and model = ?"
    private const val FIND_ONE_QUERY =
      "select maker, maturity_date, model, price from car where maker = ? and model = ?"
    private const val FIND_ALL_QUERY = "select maker, maturity_date, model, price from car"
    private fun toCar(row: ResultSet) = Car(
      make = CarMake.values()[row.getInt(1)],
      maturityDate = row.getTimestamp(2).toInstant(),
      model = row.getString(3),
      price = row.getInt(4)
    )
  }

  private val createPS = { connection.prepareCall(INSERT_QUERY) }
  private val updatePS = { connection.prepareCall(UPDATE_QUERY) }
  private val findOnePS = { connection.prepareCall(FIND_ONE_QUERY) }
  private val findAllPS = { connection.prepareCall(FIND_ALL_QUERY) }
  private val removePS = { connection.prepareCall(DELETE_QUERY) }


  private fun find(carId: CarId) = find(carId.make, carId.model)
  private fun find(maker: CarMake, model: String): Car? {
    val query = findOnePS()
    query.setInt(1, maker.ordinal)
    query.setString(2, model)
    val rs = query.executeQuery()
    return if (rs.next()) toCar(rs) else null
  }

  override fun create(message: Message<Car>) {
    val car = message.body()

    val found = find(car.make, car.model)
    if (found != null) {
      message.fail(1, "already exists")
    }

    val ps = createPS()
    ps.setInt(1, car.make.ordinal)
    ps.setTimestamp(2, Timestamp.from(car.maturityDate))
    ps.setString(3, car.model)
    ps.setInt(4, car.price)
    ps.execute()
    message.reply(null)
  }

  override fun update(message: Message<Car>) {
    val car = message.body()

    val found = find(car.make, car.model)
    if (found == null) {
      message.fail(1, "not found")
    }

    val ps = updatePS()
    ps.setTimestamp(1, Timestamp.from(car.maturityDate))
    ps.setInt(2, car.price)
    ps.setInt(3, car.make.ordinal)
    ps.setString(4, car.model)
    ps.execute()
    message.reply(null)
  }

  override fun list(message: Message<Unit>) {
    val query = findAllPS()
    val rs = query.executeQuery()
    val list = rs.iterable(::toCar).toList()
    message.reply(list, DeliveryOptions().setCodecName("List"))
  }


  override fun find(message: Message<CarId>) {
    val car = find(message.body())
    if (car == null) {
      message.fail(1, "not found")
    } else {
      message.reply(car)
    }
  }

  override fun delete(message: Message<CarId>) {
    val carId = message.body()
    val ps = removePS()
    ps.setInt(1, carId.make.ordinal)
    ps.setString(2, carId.model)
    ps.execute()
    message.reply(null)
  }

  override fun close() {
    connection.close()
  }
}
