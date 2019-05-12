package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class RequiredException(
  val field: String?
) : Exception("$field is empty"),
  ErrorResponse {
  override val statusCode: Int get() = 400
}
