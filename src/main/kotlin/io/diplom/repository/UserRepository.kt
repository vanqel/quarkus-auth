package io.diplom.repository

import io.diplom.models.UserEntity
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class UserRepository(
    val entityManager: Mutiny.SessionFactory
) {


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
    fun checkExistsUsername(username: String?, email: String?, phone: String?) =
        entityManager.withSession { session ->
            session.createQuery(
                "select exists(select 1 u from UserEntity u where username = :username or email = :email or phone = :phone)",
                Boolean::class.java
            ).setParameter("username", username)
                .setParameter("email", email)
                .setParameter("phone", phone)
                .singleResultOrNull

        }

    /**
     * Проверка на существование пользователя по параметрам
     */
    @WithTransaction
    fun blockUnblockUser(id: Long): Uni<Boolean> = entityManager.withSession { session ->
        session.createQuery(
            "update UserEntity u set u.isBlocked = (not u.isBlocked) where id = :id",
            Int::class.java
        ).setParameter("id", id)
            .singleResult
    }.map { it > 0 }

}
