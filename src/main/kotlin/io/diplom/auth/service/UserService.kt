package io.diplom.auth.service

import io.diplom.auth.dto.inp.LoginInput
import io.diplom.auth.dto.inp.UserRegisterInput
import io.diplom.auth.dto.out.AuthOutput
import io.diplom.exception.LoginException
import io.diplom.exception.PasswordException
import io.diplom.outer.user.models.UserEntity
import io.diplom.outer.user.repository.UserRepository
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.query.Page

@ApplicationScoped
class UserService(
    private val jwtProvider: JwtProvider,
    private val repository: UserRepository
) {

    /**
     * Разлогинивание пользователя
     */
    fun logout(ex: RoutingContext) = jwtProvider.logout(ex)

}

