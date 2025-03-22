package io.diplom.dto

import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
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
     * Номер телефона пользователя
     */
    var phone: String? = null,

    /**
     * Данные пользователя
     */
    var person: PersonEntity? = null,

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
                phone = userEntity.phone,
                person = userEntity.person,
                createdAt = userEntity.createdAt
            )
        }
    }
}
