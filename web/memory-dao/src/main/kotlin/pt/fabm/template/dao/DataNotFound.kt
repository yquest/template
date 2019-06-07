package pt.fabm.template.dao

import pt.fabm.template.ErrorResponse

class DataNotFound: Exception("Not found"), ErrorResponse
