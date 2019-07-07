package pt.fabm.template.dao

import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.Login
import pt.fabm.template.models.UserRegisterIn
import java.io.Closeable
import java.sql.Connection

class UserDaoPostgres(private val connection: Connection) : Closeable, UserDao {
  companion object {
    const val INSERT_QUERY = """insert into app_user(username,password_hash,email) values (?,?,?)"""
    const val FIND_ONE_PASSWORD_QUERY = """select password_hash from app_user where username = ?"""
  }

  private val insertPS = { connection.prepareStatement(INSERT_QUERY) }
  private val findOnePass = { connection.prepareStatement(FIND_ONE_PASSWORD_QUERY) }

  private fun findUserHashPass(username: String): ByteArray? {
    val ps = findOnePass()
    ps.setString(1, username)
    val rs = ps.executeQuery()
    return if (rs.next()) rs.getBytes(1) else null
  }

  override fun create(message: Message<UserRegisterIn>) {
    val userRegisterIn = message.body()
    val hash = findUserHashPass(userRegisterIn.name)
    if (hash != null) {
      message.fail(1, "user already exists")
      return
    }
    val ps = insertPS()
    ps.setString(1, userRegisterIn.name)
    ps.setBytes(2, userRegisterIn.pass)
    ps.setString(3, userRegisterIn.email)
    ps.execute()
    message.reply(null)
  }

  override fun login(message: Message<Login>) {
    val login = message.body()
    val passHash = findUserHashPass(login.username)
    if (passHash == null) {
      message.reply(false)
      return
    }
    message.reply(passHash.contentEquals(login.password))
  }


  override fun close() {
    connection.close()
  }

}
