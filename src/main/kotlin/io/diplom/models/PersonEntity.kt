package io.diplom.models

import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import java.time.LocalDate

@Entity
@Table(name = "person")
class PersonEntity(
    /**
     * Имя пользователя
     */
    @Column(name = "name")
    var name: String? = null,

    /**
     * Фамилия пользователя
     */
    @Column(name = "surname")
    var surname: String? = null,

    /**
     * Отчество пользователя
     */
    @Column(name = "second_name")
    var secondName: String? = null,

    /**
     * Отчество пользователя
     */
    @Column(name = "birth_date")
    var birthDate: LocalDate? = null,


    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], orphanRemoval = true)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(
        name = "person_id",
        updatable = false,
        insertable = false,
        referencedColumnName = "id"
    )
    var documents: MutableList<PersonDocuments> = mutableListOf()

) : PanacheEntity() {

    companion object

}
