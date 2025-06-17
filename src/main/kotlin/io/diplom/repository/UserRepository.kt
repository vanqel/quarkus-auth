package io.diplom.repository

import io.diplom.dto.inp.InputPersonEntity
import io.diplom.models.PersonDocuments
import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.query.Page
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class UserRepository(
    val entityManager: Mutiny.SessionFactory,
    val personRepo: PanacheRepository<PersonEntity>,
    val documentRepo: PanacheRepository<PersonDocuments>
) {

    /**
     * Поиск пользователя по параметрам
     */
    fun findAll(page: Page): Uni<List<UserEntity>> = entityManager.withSession { session ->
        session.createQuery(
            "select u from UserEntity u",
            UserEntity::class.java
        ).setPage(page).resultList
    }

    @WithTransaction
    fun updatePerson(personEntity: InputPersonEntity): Uni<PersonEntity> =
        personRepo.findById(personEntity.id!!)
            .call { pe ->
                pe.documents.clear()
                personRepo.persistAndFlush(pe)
            }.flatMap { pe ->
                pe.name = personEntity.name
                pe.surname = personEntity.surname
                pe.secondName = personEntity.secondName
                pe.birthDate = personEntity.birthDate
                pe.documents.addAll(personEntity.getDocsEntity())
                personRepo.persistAndFlush(pe)
            }

    /**
     * Поиск пользователя по параметрам
     */
    fun findByUsername(username: String): Uni<UserEntity> = entityManager.withSession { session ->
        session.createQuery(
            "select u from UserEntity u join fetch u.roles r where u.username = :username",
            UserEntity::class.java
        ).setParameter("username", username)
            .singleResultOrNull
    }


    /**
     * Поиск пользователя по параметрам
     */
    @WithTransaction
    fun findByParams(payload: String): Uni<UserEntity?> =
        entityManager.withSession { session ->
            session.createQuery(
                "select u from UserEntity u join fetch u.roles r where u.username = :username or u.email = :email or u.phone = :phone",
                UserEntity::class.java
            ).setParameter("username", payload)
                .setParameter("email", payload)
                .setParameter("phone", payload)
                .singleResultOrNull
        }

    /**
     * Проверка на существование пользователя по параметрам
     */
    @WithTransaction
    fun checkExistsUsername(email: String?, phone: String?): Uni<Boolean> =
        entityManager.withSession { session ->
            session.createQuery(
                "select exists(select 1 u from UserEntity u where email = :email or phone = :phone)",
                Boolean::class.java
            ).setParameter("email", email)
                .setParameter("phone", phone)
                .singleResultOrNull

        }

    /**
     * Проверка на существование пользователя по параметрам
     */
    @WithTransaction
    fun blockUnblockUser(id: Long): Uni<Boolean> = entityManager.withTransaction { session ->
        session.createQuery(
            "update UserEntity u set u.isBlocked = (not u.isBlocked) where id = :id",
            Int::class.java
        ).setParameter("id", id)
            .singleResult
    }.map { it > 0 }

}
