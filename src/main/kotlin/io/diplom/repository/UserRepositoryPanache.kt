package io.diplom.repository

import io.diplom.models.UserEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepositoryPanache: PanacheRepository<UserEntity>

