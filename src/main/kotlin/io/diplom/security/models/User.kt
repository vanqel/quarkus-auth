package io.diplom.security.models

import io.diplom.models.PersonEntity
import java.security.Principal
import java.time.LocalDateTime


class User(

    var id: Long,
    /**
     * Логин пользователя
     */
    var username: String,
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
    var person: PersonEntity,

    /**
     * Дата регистрации пользователя
     */
    var createdAt: LocalDateTime,

    /**
     * Признак блокировки пользователя
     */
    var isBlocked: Boolean? = false,

    val roles: List<Authority>,

    ) : Principal {

    fun hasAuthority(
        authorityName: AuthorityName
    ): Boolean {
        return this.roles
            .any { a: Authority -> authorityName == a.name }
    }

    fun hasAuthority(
        vararg authorityName: AuthorityName
    ): Boolean {
        return this.roles
            .any { a: Authority -> a.name in authorityName }
    }


    val orgOidRestriction: String?
        get() = null

    fun hasAnyAuthority(authorityNames: Collection<AuthorityName>): Boolean {
        return true
    }

    override fun getName(): String = username!!
}
