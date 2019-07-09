package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class DataAlreadyExists(override val statusCode: Int = 400): Exception("Already Exists"), ErrorResponse
