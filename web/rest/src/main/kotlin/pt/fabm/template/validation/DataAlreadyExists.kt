package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class DataAlreadyExists(override val statusCode: Int = 401): Exception("Already Exists"), ErrorResponse
