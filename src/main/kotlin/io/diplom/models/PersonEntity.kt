package io.diplom.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
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
    val name: String? = null,

    /**
     * Фамилия пользователя
     */
    @Column(name = "surname")
    val surname: String? = null,

    /**
     * Отчество пользователя
     */
    @Column(name = "second_name")
    val secondName: String? = null,

    /**
     * Отчество пользователя
     */
    @Column(name = "birth_date")
    val birthDate: LocalDate? = null,


    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(
        name = "person_id",
        updatable = false,
        insertable = false,
        referencedColumnName = "id"
    )
    val documents: MutableList<PersonDocuments> = mutableListOf()

) : PanacheEntity() {

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.JOIN)
    @JoinColumn(
        name = "id",
        updatable = false,
        insertable = false,
        referencedColumnName = "person_id"
    )
    var user: UserEntity? = null

}
