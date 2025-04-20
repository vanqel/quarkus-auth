package io.diplom.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.diplom.security.models.Authority
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
            phone = userEntity.phone,
            person = userEntity.person,
            createdAt = userEntity.createdAt,
            user = userEntity,
            roles = userEntity.roles.mapNotNull { it.role?.authority() }
        )
    }
}
