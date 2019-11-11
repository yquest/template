package pt.fabm.template.dao.model

import io.vertx.core.json.JsonObject
import pt.fabm.template.models.type.UserRegisterIn

data class User(
  val name: String,
  val pass: ByteArray,
  val email: String?
){
  companion object{
    fun toUser(json:JsonObject):User{
      return User(
        name = json.getString("user"),
        pass = json.getBinary("pass"),
        email = json.getString("email")
      )
    }
    fun toUser(registerIn: UserRegisterIn):User{
      return User(
        name = registerIn.name,
        pass = registerIn.pass,
        email = registerIn.email
      )
    }
  }
  fun toJson():JsonObject{
    return JsonObject()
      .put("user",name)
      .put("pass",pass)
      .put("email",email)
  }
}
