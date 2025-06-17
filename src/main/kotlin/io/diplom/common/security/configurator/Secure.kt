package io.diplom.common.security.configurator

import io.diplom.common.security.models.User
import io.diplom.exception.AuthException
import io.quarkus.security.identity.SecurityIdentity


fun SecurityIdentity.getUser(): User {
    if (this.isAnonymous) throw AuthException()
    else return this.principal as User
}
