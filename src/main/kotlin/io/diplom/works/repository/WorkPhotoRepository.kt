package io.diplom.works.repository

import io.diplom.works.models.WorkPhotoEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class WorkPhotoRepository: PanacheRepository<WorkPhotoEntity>
