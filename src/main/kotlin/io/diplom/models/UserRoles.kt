package io.diplom.models

import io.diplom.security.models.AuthorityName
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * Роли пользователей
 */
@Entity
@Table(name = "user_roles")
class UserRoles(
    /**
     * Пользователь
     */
    @ManyToOne
    val uid: UserEntity? = null,

    /**
     * Роль пользователя
     */
    @Column(nullable = false)
    val role: AuthorityName? = null

) : PanacheEntity()
