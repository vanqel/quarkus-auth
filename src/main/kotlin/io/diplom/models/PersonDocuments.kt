package io.diplom.models

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "documents")
class PersonDocuments(

    @Column(nullable = false)
    val serial: String? = null,

    @Column(nullable = false)
    val number: String? = null,

    @Column(nullable = false)
    val authority: String? = null,

    @Column(nullable = false)
    val dateIssue: LocalDate? = null,

    @Column(nullable = false)
    val type: DocType? = null,

    @Column(nullable = false, name = "person_id")
    var personId: Long? = null,

    @Column(nullable = false)
    var isApproved: Boolean = false,

    @Column
    var userApproved: Long? = null

) : PanacheEntity() {

    enum class DocType {
        PASSPORT, OMS, CV
    }
}
