package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class AuthException : Exception("Autentication Fails"), ErrorResponse{
  override val statusCode: Int get() = 401
}
