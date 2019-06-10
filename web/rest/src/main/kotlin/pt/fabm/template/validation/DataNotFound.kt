package pt.fabm.template.validation

import pt.fabm.template.ErrorResponse

class DataNotFound: Exception("Not found"), ErrorResponse
