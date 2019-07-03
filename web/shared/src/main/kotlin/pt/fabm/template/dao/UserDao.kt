package pt.fabm.template.dao

import io.vertx.reactivex.core.eventbus.Message
import pt.fabm.template.models.Login
import pt.fabm.template.models.UserRegisterIn

interface UserDao {
  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.User.create]
   *
   * reply: none
   */
  fun create(message: Message<UserRegisterIn>)

  /**
   * method called in address [pt.fabm.template.EventBusAddresses.Dao.User.login]
   *
   * reply: [Boolean]
   */
  fun login(message: Message<Login>)
}
