package io.diplom.api.http

import io.diplom.dto.inp.InputPersonEntity
import io.diplom.exception.AuthException
import io.diplom.models.PersonEntity
import io.diplom.security.configurator.getUser
import io.diplom.security.models.AuthorityName
import io.diplom.service.UserService
import io.quarkus.security.identity.SecurityIdentity
import io.quarkus.vertx.web.Body
import io.quarkus.vertx.web.Param
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RouteBase
import io.smallrye.mutiny.Uni
import jakarta.annotation.security.RolesAllowed
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.core.MediaType

@ApplicationScoped
@RouteBase(path = "admin-api")
class AdminRoute(
    val authService: UserService,
    private val securityIdentity: SecurityIdentity,
    private val userService: UserService
) {

    @Route(
        path = "block",
        methods = [Route.HttpMethod.GET],
    )
    fun blockUser(
        @Param("id") id: String?
    ) = authService.blockUser(id!!.toLong())



    /**
     * Аутентификация пользователя
     */
    @Route(
        path = "/user/update",
        methods = [Route.HttpMethod.PUT],
        produces = [MediaType.APPLICATION_JSON]
    )
    fun upd(
        @Body personRequest: InputPersonEntity
    ): Uni<PersonEntity> {
        val user = securityIdentity.getUser()

        if (!user.hasAuthority(AuthorityName.ADMIN))
            throw AuthException()

        return userService.updateMe(personRequest)
    }

    @Route(
        path = "list",
        methods = [Route.HttpMethod.GET],
    )
    fun listUser(
        @Param("size") size: Int?,
        @Param("page") page: Int?
    ) = authService.allUsers(size!!, page!!)
}
