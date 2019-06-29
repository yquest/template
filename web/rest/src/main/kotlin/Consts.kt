import io.jsonwebtoken.security.Keys
import java.util.*

object Consts {
  val USER_NAME_COOKIE: String = "user_name"
  const val ACCESS_TOKEN_COOKIE = "access_token"
  private val PROPS = loadProps()
  val PASS_PHRASE get() = PROPS[0]
  val PUBLIC_DIR get() = PROPS[1]
  val SIGNING_KEY = Keys.hmacShaKeyFor(PASS_PHRASE.toByteArray())!!

  private fun loadProps(): Array<String> {
    val props = Properties()
    props.load(Consts::class.java.getResourceAsStream("/app.properties"))
    return arrayOf(
      props.getProperty("key"),
      props.getProperty("pdir")
    )
  }
}
