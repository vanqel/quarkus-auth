package io.diplom.repository

import io.diplom.models.PersonDocuments
import io.quarkus.hibernate.reactive.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class DocumentsRepositoryPanache: PanacheRepository<PersonDocuments>
