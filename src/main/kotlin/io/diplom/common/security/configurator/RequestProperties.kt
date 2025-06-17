package io.diplom.common.security.configurator


data class RequestProperties(
    val filters: List<AuthenticationFilter>,
    val roles: List<String>
)
