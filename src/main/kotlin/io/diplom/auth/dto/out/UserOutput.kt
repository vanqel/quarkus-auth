package io.diplom.auth.dto.out

import io.diplom.outer.user.models.UserEntity
import java.time.LocalDateTime

class UserOutput(
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
    var isBlocked: Boolean? = false
) {
    companion object {
        fun fromEntity(userEntity: UserEntity): UserOutput {
            return UserOutput(
                username = userEntity.username,
                email = userEntity.email,
                about = userEntity.about,
                skills = userEntity.skills,
                directions = userEntity.directions,
                isApproved = userEntity.isApproved,
                createdAt = userEntity.createdAt
            )
        }
    }
}
