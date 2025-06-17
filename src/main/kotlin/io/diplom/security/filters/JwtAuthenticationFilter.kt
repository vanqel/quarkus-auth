package io.diplom.security.filters

import io.diplom.exception.AuthException
import io.diplom.security.configurator.AuthenticationFilter
import io.diplom.security.models.User
import io.diplom.service.JwtProvider
import io.diplom.service.JwtProvider.Companion.PREFIX_VALUE
import io.diplom.service.UserService
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Uni
import io.vertx.ext.auth.impl.jose.JWT
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Named
import jakarta.ws.rs.core.HttpHeaders

/**
 * Фильтр авторизации на основе JWT токена.
 */
@ApplicationScoped
@Named("jwt-auth")
class JwtAuthenticationFilter(
    private val jwtProvider: UserService,
) : AuthenticationFilter {

    /**
     * Аутентификация на основе JWT токена.
     */
    override fun authenticate(
        context: RoutingContext,
    ): Uni<User> = Uni.createFrom().context { c ->

        val tokenHeader = context.request().headers()[HttpHeaders.AUTHORIZATION]

        val tokenCookie = context.request().cookies().firstOrNull { it.name == JwtProvider.COOKIE_NAME }?.value


        val token = (tokenHeader ?: tokenCookie?.let {
            PREFIX_VALUE + it
        }) ?: return@context Uni.createFrom().failure(AuthException())

        /**
         * Не валидный JWT токен
         */
        if (!token.startsWith("Bearer ")) return@context Uni.createFrom().failure(AuthException())

        val claims = JWT.parse(token.substring("Bearer ".length))
        val payload = claims.getJsonObject("payload")
        val signature = claims.getString("signature")

        /**
         * Проверяем срок действия токена и наличие тела и подписи токена.
         */
        if (payload == null ||
            signature == null ||
            payload.getLong("exp") < System.currentTimeMillis() / 1000
        ) Uni.createFrom().failure(AuthException())

        else Uni.createFrom().item(payload.getString("username"))
    }.flatMap {
        jwtProvider.loginByUsername(it, context)
    }.map {
        it.user?.toUser()
    }
}
