package io.diplom.models

import io.diplom.security.models.Authority
import io.diplom.security.models.User
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(

    /**
     * Логин пользователя
     */
    @Column(name = "username", nullable = false)
    var username: String? = null,

    /**
     * Пароль пользователя
     */
    @Column(name = "password")
    var password: String? = null,

    /**
     * Электронная почта пользователя
     */
    @Column(name = "email")
    var email: String? = null,


    /**
     * Номер телефона пользователя
     */
    @Column(name = "phone")
    var phone: String? = null,

    /**
     * Данные пользователя
     */
    @OneToOne(fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.JOIN) @JoinColumn(name = "person_id", nullable = false)
    var person: PersonEntity? = null,

    /**
     * Дата регистрации пользователя
     */
    @Column(name = "created_at", updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    /**
     * Признак блокировки пользователя
     */
    @Column(name = "is_blocked")
    var isBlocked: Boolean = false

) : PanacheEntity() {

    /**
     * Роли пользователя
     */
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "uid_id", referencedColumnName = "id")
    val roles: List<UserRoles> = emptyList()

    fun toUser(): User {
        return User(
            id = id!!,
            username = username!!,
            email = email,
            phone = phone,
            person = person!!,
            roles = roles.map { Authority(it.role!!) },
            createdAt = createdAt,
            isBlocked = isBlocked
        )
    }

}
