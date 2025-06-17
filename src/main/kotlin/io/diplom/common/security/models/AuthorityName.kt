package io.diplom.common.security.models

enum class AuthorityName {
    ADMIN,
    USER,
    AUTHOR;

    fun authority(): Authority = Authority(this)
}

