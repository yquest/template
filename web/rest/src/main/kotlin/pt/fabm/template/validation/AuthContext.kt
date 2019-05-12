package pt.fabm.template.validation

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.vertx.reactivex.ext.web.RoutingContext

data class AuthContext(val jwt: Jws<Claims>, val rc: RoutingContext)
