package io.diplom.dto

import io.diplom.models.PersonDocuments
import io.diplom.models.PersonDocuments.DocType
import java.time.LocalDate

class InputPersonEntity(

    var id: Long? = null,

    /**
     * Имя пользователя
     */
    var name: String? = null,

    /**
     * Фамилия пользователя
     */
    var surname: String? = null,

    /**
     * Отчество пользователя
     */
    var secondName: String? = null,

    /**
     * Отчество пользователя
     */
    var birthDate: LocalDate? = null,


    var documents: MutableList<InputPersonDocuments> = mutableListOf()

)


class InputPersonDocuments(

    var id: Long? = null,

    val serial: String? = null,

    val number: String? = null,

    val authority: String? = null,

    val dateIssue: LocalDate? = null,

    val type: DocType? = null

) {
    fun toEntity() = PersonDocuments(
        serial, number, authority, dateIssue, type
    )
}
