package pt.fabm.template

interface ErrorResponse {
  val statusCode: Int
    get() = 500
  val message: String?
}
