package pt.fabm.template.models.type

data class UserRegisterIn(
  val name: String,
  val email: String,
  val pass: ByteArray
){
  companion object Fields{
    const val NAME = "name"
    const val PASS = "pass"
    const val EMAIL = "email"
  }
}
