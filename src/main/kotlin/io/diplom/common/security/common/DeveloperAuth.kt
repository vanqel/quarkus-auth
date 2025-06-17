package io.diplom.common.security.common

import io.diplom.auth.service.JwtProvider.Companion.COOKIE_NAME
import io.diplom.common.security.models.AuthorityName
import io.diplom.common.security.models.User
import io.diplom.exception.GeneralException
import io.quarkus.vertx.web.Param
import io.quarkus.vertx.web.Route
import io.quarkus.vertx.web.RoutingExchange
import io.smallrye.mutiny.Uni
import io.vertx.core.http.Cookie
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.time.LocalDateTime


/**
 * Доступ по токену доступа, переданному в заголовке
 */
@ApplicationScoped
class DeveloperAuth(
    @ConfigProperty(name = "security.devtoken") private val devToken: String,
) {


    /**
     * Вход по токену доступа для разработчика
     */
    @Route(
        path = "/q/auth",
        methods = [Route.HttpMethod.GET],
    )
    fun authDeveloper(
        @Param("token") token: String,
        ex: RoutingExchange
    ) {
        if (token != devToken) {
            ex.context().fail(GeneralException("Доступ к аккаунту запрещён", 403))
            return
        }

        ex.response().addCookie(Cookie.cookie(COOKIE_NAME, devToken).apply {
            this.maxAge = 600
        })

         ex.context().redirect("/q/dev-ui")
    }


    init {
        if (devToken.length < 20) {
            throw RuntimeException("Длина токена разработчика должна быть не меньше 20 символов")
        }
    }

    fun getDeveloper(): Uni<User> = Uni.createFrom().item(developerPrincipal)

    fun validateToken(token: String?): Boolean =
        token == devToken

    /**
     * developer token
     */
    private val developerPrincipal = User(
        0,
        "admin",
        "1",
        "Разработчик",
        "Hard",
        listOf("12"),
        true,
        LocalDateTime.now(),
        false,
        listOf(AuthorityName.ADMIN.authority()),
    )


}
