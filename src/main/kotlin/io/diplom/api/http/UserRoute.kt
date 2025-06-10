package io.diplom.api.http

import io.diplom.exception.AuthException
import io.diplom.models.PersonEntity
import io.diplom.security.configurator.getUser
import io.diplom.security.models.AuthorityName
import io.diplom.service.UserService
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.vertx.web.Body
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.quarkus.vertx.web.RoutingExchange
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@RouteBase(path = "/api/user")
class UserRoute(
    val userService: UserService
) {


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

    /**
     * Аутентификация пользователя
     */
    @Route(
        path = "/me/update",
        methods = [Route.HttpMethod.PUT],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun upd(
        @Body personRequest: PersonEntity
    ): Uni<PersonEntity> {
        val user = securityIdentity.getUser()
        if (personRequest.id != user.person.id && !user.hasAuthority(AuthorityName.ADMIN))
            throw AuthException()

        return userService.updateMe(personRequest)
    }


}
