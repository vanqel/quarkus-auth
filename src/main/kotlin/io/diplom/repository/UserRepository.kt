package io.diplom.repository

import io.diplom.dto.InputPersonEntity
import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.quarkus.hibernate.reactive.panache.common.WithTransaction
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import org.hibernate.query.Page
import org.hibernate.reactive.mutiny.Mutiny

@ApplicationScoped
class UserRepository(
    val entityManager: Mutiny.SessionFactory
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
    fun updatePerson(personEntity: InputPersonEntity): Uni<PersonEntity> = entityManager.withTransaction { session ->

        session.find(PersonEntity::class.java, personEntity.id)
            .call { pe ->
                pe.name = personEntity.name
                pe.surname = personEntity.surname
                pe.secondName = personEntity.secondName
                pe.birthDate = personEntity.birthDate
                pe.documents.clear()
                pe.persistAndFlush<PersonEntity>().map {
                   session.removeAll(it.documents)
                }.map {
                    pe.documents.addAll(
                        personEntity.documents
                            .map { it.toEntity().also { it.personId = pe.id } }
                    ) // фастом
                    pe.persistAndFlush<PersonEntity>()
                }.map {
                    session.merge(pe)
                }
            }.call { pe ->
                pe.persistAndFlush<PersonEntity>()

            }
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
