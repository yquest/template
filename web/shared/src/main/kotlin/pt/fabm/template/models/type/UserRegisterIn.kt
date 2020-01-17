package pt.fabm.template.models.type

data class UserRegisterIn(
  val name: String,
  val email: String,
  val pass: ByteArray
){
  companion object Fields{
    const val USER = "user"
    const val PASS = "pass"
    const val EMAIL = "email"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as UserRegisterIn

    if (name != other.name) return false
    if (email != other.email) return false
    if (!pass.contentEquals(other.pass)) return false

    return true
  }

  override fun hashCode(): Int {
    var result = name.hashCode()
    result = 31 * result + email.hashCode()
    result = 31 * result + pass.contentHashCode()
    return result
  }
}
