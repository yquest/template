package pt.fabm.template.dao

import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.Login
import pt.fabm.template.models.UserRegisterIn
import java.security.MessageDigest

class UserDaoMemory : UserDao {

  val users = mutableMapOf<String, UserRegisterIn>()

  override fun create(message: Message<UserRegisterIn>) {
    val body = message.body()
    users[body.name] = body
    message.reply(null)
  }

  override fun login(message: Message<Login>) {
    val body = message.body()
    val current = users.get(body.username)

    val auth = current?.takeIf {
      body.password.contentEquals(it.pass)
    } != null
    message.reply(auth)
  }


}
