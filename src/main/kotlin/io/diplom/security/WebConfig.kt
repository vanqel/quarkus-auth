package io.diplom.security

import io.diplom.security.configurator.SecurityConfiguration
import io.diplom.security.filters.DeveloperAuthenticationFilter
import io.diplom.security.filters.FilterType
import io.diplom.security.filters.JwtAuthenticationFilter
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped

/**
 * Конфигурация безопасности.
 */
@ApplicationScoped
class WebConfig(
    /** Фильтр аторизации разработчика.*/
    val developerAuthenticationFilter: DeveloperAuthenticationFilter,
    /** Фильтр аторизации пользователя.*/
    val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {

    /**
     * Создание конфигурации безопасности.
     */
    @ApplicationScoped
    @Startup
    fun config(): SecurityConfiguration =
        SecurityConfiguration.Builder
            .permitAll("/api/docs","/q/openapi")
            .permitAll("/q/dev-ui/*")
            .permitAll("/api/auth/*")
            .authorized("/api/user/*")
            .addFilter(developerAuthenticationFilter, FilterType.DEVELOPER)
            .addFilter(jwtAuthenticationFilter, FilterType.USER)
            .anyRequestPermitAll()
            .build()


}
