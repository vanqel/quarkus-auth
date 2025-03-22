package io.diplom.api.http

import io.diplom.security.configurator.getUser
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.quarkus.vertx.web.RoutingExchange
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@RouteBase(path = "/api/user")
class UserRoute {


    /**
     * Security context, инициализируется на уровне запроса.
     */
    @Inject
    private lateinit var securityIdentity: SecurityIdentity

    /**
     * Аутентификация пользователя
     */
    @Route(
        path = "/me",
        methods = [Route.HttpMethod.GET],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun auth(
        ex: RoutingExchange
    ) = securityIdentity.getUser()

}
