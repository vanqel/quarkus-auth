package io.diplom.repository

import io.diplom.models.PersonDocuments
import io.diplom.models.PersonEntity
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class PersonRepositoryPanache: PanacheRepository<PersonEntity>

