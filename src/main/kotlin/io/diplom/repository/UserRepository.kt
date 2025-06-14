package io.diplom.repository

import io.diplom.models.UserEntity
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.query.Page
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class UserRepository(
    val entityManager: Mutiny.SessionFactory,
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
    fun checkExistsPhone(phone: String?, personId: Long): Uni<Boolean> =
        entityManager.withSession { session ->
            session.createQuery(
                "select exists(select 1 u from UserEntity u where phone = :phone and person.id != :personId)",
                Boolean::class.java
            ).setParameter("phone", phone)
                .setParameter("personId", personId)

                .singleResultOrNull

        }


    /**
     * Проверка на существование пользователя по параметрам
     */
    fun checkExistsEmail(email: String?, personId: Long): Uni<Boolean> =
        entityManager.withSession { session ->
            session.createQuery(
                "select exists(select 1 u from UserEntity u where email = :email and person.id != :personId)",
                Boolean::class.java
            ).setParameter("email", email)
                .setParameter("personId", personId)
                .singleResultOrNull

        }


    /**
     * Проверка на существование пользователя по параметрам
     */
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
    fun findUserByPersonId(personId: Long): Uni<UserEntity> =
        entityManager.withSession { session ->
            session.createQuery(
                "select u from UserEntity u where person.id = :id ",
                UserEntity::class.java
            ).setParameter("id", personId)
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
