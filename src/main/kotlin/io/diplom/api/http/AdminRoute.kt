package io.diplom.api.http

import io.diplom.service.UserService
import io.quarkus.vertx.web.Param
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@RouteBase(path = "/admin-api")
class AdminRoute(
    val authService: UserService
) {

    @RolesAllowed(value = ["ADMIN"])
    @Route(
        path = "block",
        methods = [Route.HttpMethod.GET],
    )
    fun blockUser(
        @Param("id") id: String?
    ) = authService.blockUser(id!!.toLong())
}
