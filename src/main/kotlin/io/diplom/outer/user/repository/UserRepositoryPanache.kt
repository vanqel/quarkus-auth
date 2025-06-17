package io.diplom.outer.user.repository

import io.diplom.outer.user.models.UserEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepositoryPanache: PanacheRepository<UserEntity>
