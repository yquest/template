import io.jsonwebtoken.security.Keys
import java.util.*

object Consts {
  val USER_NAME_COOKIE: String = "user_name"
  const val ACCESS_TOKEN_COOKIE = "access_token"
  val PASS_PHRASE = loadPassPhrase()
  val SIGNING_KEY = Keys.hmacShaKeyFor(PASS_PHRASE.toByteArray())!!

  private fun loadPassPhrase(): String {
    val props = Properties()
    props.load(Consts::class.java.getResourceAsStream("/app.properties"))
    return props.getProperty("key")
  }
}
