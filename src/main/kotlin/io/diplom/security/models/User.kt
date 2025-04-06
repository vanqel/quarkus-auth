package io.diplom.security.models

import java.security.Principal


class User(
    val id: Long,
    val username: String,
    val personId: Long?,
    val firstName: String? = null,
    val secondName: String? = null,
    val lastName: String? = null,
    var email: String?,
    var phoneNumber: String?,
    val authorities: List<Authority>,
) : Principal {

    fun hasAuthority(
        authorityName: AuthorityName
    ): Boolean {
        return this.authorities
            .any { a: Authority -> authorityName == a.name }
    }


    val orgOidRestriction: String?
        get() = null

    fun hasAnyAuthority(authorityNames: Collection<AuthorityName>): Boolean {
        return true
    }

    override fun getName(): String = username
}
