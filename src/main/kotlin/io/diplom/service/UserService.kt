package io.diplom.service

import io.diplom.dto.AuthOutput
import io.diplom.dto.InputPersonEntity
import io.diplom.dto.LoginInput
import io.diplom.dto.UserInput
import io.diplom.dto.UserOutput
import io.diplom.exception.ExistException
import io.diplom.exception.GeneralException
import io.diplom.exception.LoginException
import io.diplom.exception.PasswordException
import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.diplom.models.UserRoles
import io.diplom.repository.UserRepository
import io.diplom.security.models.AuthorityName
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.hibernate.query.Page

@ApplicationScoped
class UserService(
    private val jwtProvider: JwtProvider,
    private val repository: UserRepository
) {

    /**
     * Регистрация пользователя
     */
    @Transactional
    @WithTransaction
    fun registerUser(user: UserInput): Uni<UserOutput> =
        repository.checkExistsUsername(user.email, user.phoneNumber).flatMap {
            if (it) {
                Uni.createFrom().failure(ExistException())
            } else {
                Uni.createFrom().item(user)
            }
        }.flatMap {
            user.toPersonEntity().persist<PersonEntity>()
        }.flatMap { p ->
            user.toUserEntity().apply {
                this.person = p
                this.password = BcryptUtil.bcryptHash(this.password!!)
            }.persist<UserEntity>()
        }.flatMap { us ->

            if (user.role == AuthorityName.ADMIN)
                Uni.createFrom().failure(GeneralException("Нельзя создать администратора", 500))
            else UserRoles(us, user.role)
                .persist<UserRoles>()
                .map { us }
        }.map {
            UserOutput.fromEntity(it)
        }

    /**
     * Авторизация пользователя по логину
     */
    @WithTransaction
    fun loginByUsername(payload: String, ex: RoutingContext): Uni<AuthOutput> =
        repository.findByUsername(payload).flatMap {
            it?.let {
                Uni.createFrom().item(it)
            } ?: Uni.createFrom().failure(LoginException())
        }.flatMap { user ->
            jwtProvider.setToken(user, ex)
        }

    /**
     * Авторизация пользователя по логину, почте, телефону и паролю
     */
    @WithTransaction
    fun login(payload: LoginInput, ex: RoutingContext): Uni<AuthOutput> =
        repository.findByParams(payload.payload).flatMap {
            it ?: run { return@flatMap Uni.createFrom().failure(LoginException()) }

            if (BcryptUtil.matches(payload.password, it.password!!)) {
                Uni.createFrom().item(it)
            } else {
                Uni.createFrom().failure(PasswordException())
            }
        }.flatMap { user ->
            jwtProvider.setToken(user, ex)
        }


    /**
     * Разлогинивание пользователя
     */
    fun logout(ex: RoutingContext) = jwtProvider.logout(ex)

    /**
     * Блокировка / разблокировка пользователя
     */
    @WithTransaction
    fun blockUser(id: Long): Uni<Boolean> = repository.blockUnblockUser(id)


    /**
     * Блокировка / разблокировка пользователя
     */
    @WithTransaction
    fun allUsers(size: Int, page: Int): Uni<List<UserEntity>> = repository.findAll(Page.page(size, page))

    @WithTransaction
    fun updateMe(personEntity: InputPersonEntity) = repository.updatePerson(personEntity)


}

