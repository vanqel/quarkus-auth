package io.diplom.auth.dto.inp

import io.diplom.common.security.models.AuthorityName
import io.diplom.outer.user.models.UserEntity

data class UserRegisterInput(
    val password: String,
    val username: String,
    val name: String,
    val about: String?,
    val skills: String?,
    val directions: List<String>?,
    val email: String,
    val role: AuthorityName?,
) {
    fun toUserEntity() = UserEntity(
        username = username,
        password = password,
        email = email,
        directions = directions ?: emptyList(),
        skills = skills,
        about = about
    )
}
