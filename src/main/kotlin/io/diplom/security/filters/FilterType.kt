package io.diplom.security.filters

import io.diplom.security.configurator.AuthOrder

enum class FilterType(
    override val order: Int,
) : AuthOrder {
    DEVELOPER(1), USER(2)
}
