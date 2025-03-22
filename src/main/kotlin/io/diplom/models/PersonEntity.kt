package io.diplom.models

import com.fasterxml.jackson.annotation.JsonIgnore
import io.quarkus.hibernate.reactive.panache.PanacheEntity
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

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
    val secondName: String? = null

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
