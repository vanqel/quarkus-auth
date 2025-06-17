package io.diplom.outer.user.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.diplom.common.security.models.AuthorityName
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    val uid: UserEntity? = null,

    /**
     * Роль пользователя
     */
    @Column(nullable = false)
    val role: AuthorityName? = null

) : PanacheEntity()
