package io.diplom.auth.dto.out

import com.fasterxml.jackson.annotation.JsonIgnore
import io.diplom.common.security.models.Authority
import io.diplom.outer.user.models.UserEntity
import java.time.LocalDateTime

class AuthOutput(

    var id: Long? = null,

    /**
     * Логин пользователя
     */
    var username: String? = null,


    /**
     * Электронная почта пользователя
     */
    var email: String? = null,


    /**
     * О пользователе
     */
    var about: String? = null,

    /**
     * Скиллы пользователя
     */
    var skills: String? = null,

    /**
     * Скиллы пользователя
     */
    var directions: List<String>,

    /**
     * Признак блокировки пользователя
     */
    var isApproved: Boolean = false,


    /**
     * Дата регистрации пользователя
     */
    var createdAt: LocalDateTime? = null,

    /**
     * Признак блокировки пользователя
     */
    var isBlocked: Boolean? = false,

    var token: String? = null,

    val roles: List<Authority>,

    @JsonIgnore
    var user: UserEntity? = null
) {


    companion object {
        fun fromEntity(userEntity: UserEntity): AuthOutput = AuthOutput(
            id = userEntity.id,
            username = userEntity.username,
            email = userEntity.email,
            about = userEntity.about,
            skills = userEntity.skills,
            directions = userEntity.directions,
            isApproved = userEntity.isApproved,
            createdAt = userEntity.createdAt,
            user = userEntity,
            roles = userEntity.roles.mapNotNull { it.role?.authority() }
        )
    }
}
