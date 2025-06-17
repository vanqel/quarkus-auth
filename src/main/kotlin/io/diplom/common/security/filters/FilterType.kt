package io.diplom.common.security.filters

import io.diplom.common.security.configurator.AuthOrder

enum class FilterType(
    override val order: Int,
) : AuthOrder {
    DEVELOPER(1), USER(2)
}
