package io.diplom.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "documents")
class PersonDocuments(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    /**
     * Данные пользователя
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(name = "person_id", nullable = false)
    var personId: PersonEntity? = null,

    @Column(nullable = false)
    val serial: String? = null,

    @Column(nullable = false)
    val number: String? = null,

    @Column(nullable = false)
    val authority: String? = null,

    @Column(nullable = false)
    val dateIssue: String? = null,

    @Column(nullable = false)
    val type: DocType? = null

) {
    enum class DocType {
        PASSPORT, OMS, CV
    }
}
