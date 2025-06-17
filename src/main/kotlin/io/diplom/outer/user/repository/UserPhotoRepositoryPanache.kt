package io.diplom.outer.user.repository

import io.diplom.outer.user.models.UserPhotos
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserPhotoRepositoryPanache: PanacheRepository<UserPhotos>
