package pt.fabm.template.dao

import java.util.*

object DaoConstants {
  interface DBProps {
    val url: String
    val driver: String
    val user: String
    val password: String
  }

  val db = loadProps()

  private fun loadProps(): DBProps {
    val props = Properties()
    props.load(DaoConstants::class.java.getResourceAsStream("/dao.properties"))
    val url = props.getProperty("db.url")
    val driver = props.getProperty("db.driver")
    val user = props.getProperty("db.user")
    val password = props.getProperty("db.password")
    return object : DBProps {
      override val driver: String get() = driver
      override val user: String get() = user
      override val password: String get() = password
      override val url: String get() = url
    }
  }

}
