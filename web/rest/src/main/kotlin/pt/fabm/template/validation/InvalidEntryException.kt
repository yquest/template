package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class InvalidEntryException(value: String, field: String) : Exception("Invalid entry:$value in field $field"),
  ErrorResponse {
  override val statusCode: Int get() = 400
}
