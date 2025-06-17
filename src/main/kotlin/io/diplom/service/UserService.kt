package io.diplom.service

import io.diplom.dto.inp.InputPersonDocumentsApproved
import io.diplom.dto.inp.InputPersonEntity
import io.diplom.dto.inp.LoginInput
import io.diplom.dto.inp.UserInput
import io.diplom.dto.out.AuthOutput
import io.diplom.dto.out.UserOutput
import io.diplom.exception.ExistException
import io.diplom.exception.GeneralException
import io.diplom.exception.LoginException
import io.diplom.exception.PasswordException
import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.diplom.models.UserRoles
import io.diplom.repository.DocumentsRepositoryPanache
import io.diplom.repository.PersonRepositoryPanache
import io.diplom.repository.UserRepository
import io.diplom.repository.UserRepositoryPanache
import io.diplom.security.configurator.getUser
import io.diplom.security.models.AuthorityName
import io.quarkus.elytron.security.common.BcryptUtil
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.security.identity.SecurityIdentity
import io.smallrye.mutiny.Uni
import io.vertx.ext.web.RoutingContext
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.hibernate.query.Page
import java.util.zip.CRC32

@ApplicationScoped
class UserService(
    private val jwtProvider: JwtProvider,
    private val repository: UserRepository,

    private val personRepositoryPanache: PersonRepositoryPanache,
    private val documentsRepositoryPanache: DocumentsRepositoryPanache,
    private val userRepositoryPanache: UserRepositoryPanache
) {


    /**
     * Security context, инициализируется на уровне запроса.
     */
    @Inject
    private lateinit var securityIdentity: SecurityIdentity


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
    fun updateMe(personEntity: InputPersonEntity): Uni<PersonEntity> =
        personEntity.let {

            if (personEntity.id == null) return Uni.createFrom().failure { GeneralException("Физ лицо не найдено") }

            val uniCheckPhone = personEntity.phone?.let {
                repository.checkExistsPhone(it, personEntity.id!!).onFailure().recoverWithItem(false).flatMap {
                    if (it) Uni.createFrom().failure(GeneralException("Пользователь с данным телефоном уже существует"))
                    else Uni.createFrom().item(false)
                }
            }

            val uniCheckEmail = personEntity.email?.let {
                repository.checkExistsEmail(it, personEntity.id!!).onFailure().recoverWithItem(false).flatMap {
                    if (it) Uni.createFrom().failure(GeneralException("Пользователь с данной почтой уже существует"))
                    else Uni.createFrom().item(false)
                }
            }

            val checks = Uni.combine().all().unis<Boolean>(
                listOfNotNull(uniCheckEmail, uniCheckPhone)
            ).with { true }

            val personUpdate = personRepositoryPanache.findById(personEntity.id!!)
                .call { pe ->


                    val checkSumNew = personEntity.getDocsEntity().associateBy {
                        val crc32 = CRC32()
                        crc32.update("${it.number}_${it.serial}_${it.authority}_${it.dateIssue}_${it.type}".encodeToByteArray())
                        crc32.value
                    }

                    val checkSumActual = pe.documents.associateBy {
                        val crc32 = CRC32()
                        crc32.update("${it.number}_${it.serial}_${it.authority}_${it.dateIssue}_${it.type}".encodeToByteArray())
                        crc32.value
                    }
                    val forUpdate = checkSumNew.filterNot { it.key in checkSumActual.keys }.values.toList()
                    val forRemoval = checkSumActual.filterNot { it.key in checkSumNew.keys }.mapNotNull { it.value.id }

                    pe.documents.removeIf { it.id in forRemoval }

                    personEntity.documentsEntity.addAll(forUpdate)

                    personRepositoryPanache.persistAndFlush(pe)
                }.flatMap { pe ->
                    pe.name = personEntity.name
                    pe.surname = personEntity.surname
                    pe.secondName = personEntity.secondName
                    pe.birthDate = personEntity.birthDate
                    pe.documents.addAll(personEntity.documentsEntity)
                    personRepositoryPanache.persistAndFlush(pe)
                }


            val userUpdate = repository.findUserByPersonId(personEntity.id!!)
                .map { pe ->
                    personEntity.email?.let { pe.email = personEntity.email }
                    personEntity.phone?.let { pe.phone = personEntity.phone }
                    personEntity.password?.let { pe.password = BcryptUtil.bcryptHash(personEntity.password) }
                    userRepositoryPanache.persistAndFlush(pe)
                }

            checks.call { e ->
                userUpdate
            }.flatMap { e -> personUpdate }
        }

    @WithTransaction
    fun updateStatusDocument(input: InputPersonDocumentsApproved) =
        documentsRepositoryPanache.findById(input.id).flatMap {
            it.isApproved = input.isApproved
            it.userApproved = securityIdentity.getUser().id
            documentsRepositoryPanache.persistAndFlush(it)
        }

}

