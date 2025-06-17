package io.diplom.auth.api.http

import io.diplom.auth.usecase.UserFetchUsecase
import io.diplom.works.dto.inp.AttachImagesWorkDTO
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.quarkus.vertx.web.RoutingExchange
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.PathParam

@ApplicationScoped
@RouteBase(path = "public/user")
class PublicUserApi (
    private val fetchUsecase: UserFetchUsecase
){

    @Route(
        path = "list",
        methods = [Route.HttpMethod.GET],
    )
    fun listUser(
    ) = fetchUsecase.allUsers()

}
