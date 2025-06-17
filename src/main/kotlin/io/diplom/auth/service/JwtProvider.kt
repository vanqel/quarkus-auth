package io.diplom.auth.service

import io.diplom.auth.dto.out.AuthOutput
import io.diplom.exception.GeneralException
import io.diplom.outer.user.models.UserEntity
import io.smallrye.jwt.auth.principal.JWTParser
import io.smallrye.jwt.auth.principal.ParseException
import io.smallrye.jwt.build.Jwt
import io.smallrye.mutiny.Uni
import io.vertx.core.http.Cookie
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty

/**
 * Сервис для создания JWT токенов.
 */
@ApplicationScoped
class JwtProvider(
    /**
     * JWT Secret. Приватный ключ, используемый для подписи токена.
     */
    @ConfigProperty(name = "security.jwt.secret")
    private val jwtSecret: String,

    /**
     * JWT Expiration Time. Время жизни токена в минутах.
     */
    @ConfigProperty(name = "security.jwt.age")
    private val jwtAge: String,

    /**
     * Парсер и валидатор для токена
     */
    private val jwtParser: JWTParser
) {

    fun verify(token: String) =
        runCatching {
            jwtParser.verify(token, jwtSecret)
        }.onFailure {
            when (it) {
                is ParseException -> throw GeneralException("Невалидный токен")
                else -> throw GeneralException("Неизвестная ошибка")
            }
        }


    private fun generateToken(user: UserEntity): String {
        return Jwt.issuer("Nikita Klykoit")
            .upn(user.email)
            .claim("username", user.username)
            .expiresAt(System.currentTimeMillis() + jwtAge.toInt() * 60 * 1000)
            .signWithSecret(jwtSecret)
    }

    fun setToken(user: UserEntity, ex: RoutingContext): Uni<AuthOutput>? =
        Uni.createFrom().item {

            generateToken(user)
        }.map {
            ex.response().putHeader(HEADER_NAME, PREFIX_VALUE + it)

            ex.response().addCookie(
                Cookie.cookie(COOKIE_NAME, it)
                    .setMaxAge(jwtAge.toLong() * 60)
                    .setHttpOnly(true)
                    .setSecure(false)
                    .setPath("/")
            )

            AuthOutput.fromEntity(user).apply {
                token = it
            }
        }

    fun logout(ex: RoutingContext) {
        ex.response().removeCookie(COOKIE_NAME)
    }


    companion object {
        const val COOKIE_NAME = "access_token"
        const val HEADER_NAME = "Authorization"
        const val PREFIX_VALUE = "Bearer "
    }
}
