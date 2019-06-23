package pt.fabm.template.dao

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.vertx.core.json.JsonArray
import io.vertx.kotlin.core.json.get
import pt.fabm.template.models.Car
import pt.fabm.template.models.CarId
import pt.fabm.template.models.CarMake
import java.io.Closeable
import java.sql.Connection
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset

class CarDao(private val connection: Connection):Closeable{
  companion object {

    private const val INSERT_QUERY = "insert into car(maker,maturity_date,model,price) values (?,?,?,?)"
    private const val UPDATE_QUERY = "update car set maturity_date = ?, price = ? where maker = ? and model = ?"
    private const val DELETE_QUERY = "delete from car where maker = ? and model = ?"
    private const val FIND_ONE_QUERY =
      "select maker, maturity_date, model, price from car where maker = ? and model = ?"
    private const val FIND_ALL_QUERY = "select maker, maturity_date, model, price from car"
    private const val TRUNCATE = "truncate car";
  }
  override fun close() {
    connection.close()
  }

  private fun mapResult(result: JsonArray): Car = Car(
    make = CarMake.values()[result[0]],
    maturityDate = LocalDateTime.ofInstant(result.getInstant(1), ZoneOffset.UTC),
    model = result[2],
    price = result.getInteger(3)
  )

  private val truncatePS = { connection.createStatement().execute(TRUNCATE) }
  private val createPS = { connection.prepareCall(INSERT_QUERY) }
  private val updatePS = { connection.prepareCall(UPDATE_QUERY) }
  private val findOnePS = { connection.prepareCall(FIND_ONE_QUERY) }
  private val findAllPS = { connection.prepareCall(FIND_ALL_QUERY) }
  private val removePS = { connection.prepareCall(DELETE_QUERY) }

  fun truncate(): Completable = Completable.fromCallable(truncatePS)

  fun create(car: Car) = create(car.make, car.maturityDate, car.model, car.price)
  fun create(maker: CarMake, maturityDate: LocalDateTime, model: String, price: Int) =
    Single.fromCallable(createPS).map {
      it.setInt(1, maker.ordinal)
      it.setTimestamp(2, Timestamp.valueOf(maturityDate))
      it.setString(3, model)
      it.setInt(4, price)
      it.execute()
    }.ignoreElement()


  fun update(car: Car) = update(car.make, car.maturityDate, car.model, car.price)
  fun update(maker: CarMake, maturityDate: LocalDateTime, model: String, price: Int) =
    Single.fromCallable(updatePS).map {
      it.setTimestamp(1, Timestamp.valueOf(maturityDate))
      it.setInt(2, price)
      it.setInt(3, maker.ordinal)
      it.setString(4, model)
      it.execute()
    }.ignoreElement()

  fun find(carId: CarId): Maybe<Car> = find(carId.maker,carId.model)
  fun find(maker: CarMake, model: String): Maybe<Car> = Single.fromCallable(findOnePS)
    .map {
      it.setInt(1, maker.ordinal)
      it.setString(2, model)
      it.executeQuery()
    }.flatMapMaybe {
      if (it.next()) {
        return@flatMapMaybe Maybe.just(
          Car(
            make = CarMake.values()[it.getInt(1)],
            maturityDate = it.getTimestamp(2).toLocalDateTime(),
            model = it.getString(3),
            price = it.getInt(4)
          )
        )
      } else {
        return@flatMapMaybe Maybe.empty<Car>()
      }
    }


  fun list(): Observable<Car> = Single.fromCallable(findAllPS).map { it.executeQuery() }.flatMapObservable { rs ->
    Observable.fromIterable(ResultSetIterator(rs) {
      Car(
        make = CarMake.values()[it.getInt(1)],
        maturityDate = it.getTimestamp(2).toLocalDateTime(),
        model = it.getString(3),
        price = it.getInt(4)
      )
    })
  }

  fun remove(carId:CarId): Completable = remove(carId.maker, carId.model)
  fun remove(maker: CarMake, model: String): Completable = Single.fromCallable(removePS)
    .map {
      it.setInt(1, maker.ordinal)
      it.setString(2, model)
      it.execute()
    }.ignoreElement()


}
