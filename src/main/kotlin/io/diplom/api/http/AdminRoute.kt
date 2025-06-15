package io.diplom.api.http

import io.diplom.service.UserService
import io.quarkus.vertx.web.Param
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
@RouteBase(path = "admin-api")
class AdminRoute(
    val authService: UserService
) {

    @Route(
        path = "block",
        methods = [Route.HttpMethod.GET],
    )
    fun blockUser(
        @Param("id") id: String?
    ) = authService.blockUser(id!!.toLong())

    @Route(
        path = "list",
        methods = [Route.HttpMethod.GET],
    )
    fun listUser(
        @Param("size") size: Int?,
        @Param("page") page: Int?
    ) = authService.allUsers(size!!, page!!)
}
