package io.diplom.security.filters

import io.diplom.security.common.DeveloperAuth
import io.diplom.security.configurator.AuthenticationFilter
import io.diplom.security.models.User
import io.diplom.service.JwtProvider
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import jakarta.ws.rs.core.HttpHeaders

/**
 * Фильтр авторизации на основе JWT токена.
 */
@ApplicationScoped
@Named("dev-auth")
class DeveloperAuthenticationFilter(
    private val developerAuth: DeveloperAuth,
) : AuthenticationFilter {

    /**
     * Авторизация разработчика по токену доступа.
     */
    override fun authenticate(
        context: RoutingContext,
    ): Uni<User>? {

        val tokenFromHeader = if (context.request().headers().contains(HttpHeaders.AUTHORIZATION))
            context.request().headers()[HttpHeaders.AUTHORIZATION]
        else null

        val tokenFromCookie = context.request().cookies().find { it.name == JwtProvider.COOKIE_NAME }?.value

        var found = false

        if (developerAuth.validateToken(tokenFromHeader) || developerAuth.validateToken(tokenFromCookie)) {
            found = true
        }

        return if (found) developerAuth.getDeveloper() else null
    }

}

