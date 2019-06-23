package pt.fabm.template.dao

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import pt.fabm.template.models.UserRegisterIn
import java.io.Closeable
import java.sql.Connection

class UserDao(private val connection: Connection) : Closeable {
  companion object {

    const val INSERT_QUERY = """insert into "user"(username,password_hash,email) values (?,?,?)"""
    const val UPDATE_QUERY = """update "user" set email = ?, password_hash = ? where username = ?"""
    const val FIND_ONE_PASSWORD_QUERY = """select password_hash from "user" where username = ?"""
    const val TRUNCATE_QUERY = """truncate "user""""
    const val REMOVE_QUERY = """delete from "user" where username = ?"""
  }

  private val insertPS = { connection.prepareCall(INSERT_QUERY) }

  private val updatePS = { connection.prepareCall(UPDATE_QUERY) }
  private val truncatePS = { connection.createStatement().execute(TRUNCATE_QUERY) }
  private val findOnePass = { connection.prepareCall(FIND_ONE_PASSWORD_QUERY) }
  private val removePS = { connection.prepareCall(REMOVE_QUERY) }

  override fun close() {
    connection.close()
  }

  fun create(userRegisterIn: UserRegisterIn): Completable = Single.just(insertPS())
    .map { ps ->
      ps.setString(1, userRegisterIn.name)
      ps.setBytes(2, userRegisterIn.pass)
      ps.setString(3, userRegisterIn.email)
      ps.execute()
    }.ignoreElement()

  fun truncate(): Completable = Completable.fromCallable(truncatePS)

  fun update(userName: String, passwordHash: ByteArray, email: String): Completable =
    Single.just(updatePS()).map { cs ->
      cs.setString(1, email)
      cs.setBytes(2, passwordHash)
      cs.setString(3, userName)
      cs.execute()
    }.ignoreElement()

  fun getUserPass(userName: String): Maybe<ByteArray> =
    Maybe.just(findOnePass()).map { cs ->
      cs.setString(1, userName)
      cs.executeQuery()
    }.flatMap { rs ->
      if (rs.next()) Maybe.just(rs.getBytes(1)) else Maybe.empty()
    }

  fun remove(name: String): Completable = Completable.fromCallable {
    val ps = removePS()
    ps.setString(1, name)
    ps.execute()
  }

}
