package pt.fabm.template.dao

import pt.fabm.template.ErrorResponse

class DataAlreadyExists: Exception("Already Exists"), ErrorResponse
