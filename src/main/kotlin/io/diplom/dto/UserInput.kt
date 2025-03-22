package io.diplom.dto

import io.diplom.models.PersonEntity
import io.diplom.models.UserEntity
import io.diplom.models.UserRoles
import io.diplom.security.models.AuthorityName
import java.time.LocalDate

data class UserInput(
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val secondName: String?,

    val phoneNumber: String?,
    val email: String?,

    val role: AuthorityName,
    val birthDate: LocalDate?,
) {
    fun toUserEntity() = UserEntity(
        username = login,
        password = password,
        email = email,
        phone = phoneNumber,
    )

    fun toPersonEntity() = PersonEntity(
        name = firstName,
        surname = lastName,
        secondName = secondName,
    )
}
