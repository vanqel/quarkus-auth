package io.diplom.api.http

import io.diplom.dto.inp.LoginInput
import io.diplom.dto.inp.UserInput
import io.diplom.service.UserService
import io.quarkus.vertx.web.Body
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@RouteBase(path = "/api/auth")
class AuthRoute(
    val authService: UserService
) {

    /**
     * Аутентификация пользователя
     */
    @Route(
        path = "/auth",
        methods = [Route.HttpMethod.POST],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun auth(
        @Body authRequest: LoginInput,
        ex: RoutingContext
    ) = authService.login(authRequest, ex)

    /**
     * Регистрация пользователя
     */
    @Route(
        path = "/register",
        methods = [Route.HttpMethod.POST],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun register(
        @Body authRequest: UserInput,
        ex: RoutingContext
    ) = authService.registerUser(authRequest)


    /**
     * разблокировка пользователя
     */
    @Route(
        path = "/block-user/{id}",
        methods = [Route.HttpMethod.DELETE],
    )
    fun logout(
        ex: RoutingContext
    ) = authService.logout(ex)


}
