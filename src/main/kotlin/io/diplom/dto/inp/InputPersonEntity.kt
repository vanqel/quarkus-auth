package io.diplom.dto.inp

import io.diplom.models.PersonDocuments
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

    /**
     * Почта пользователя
     */
    var email: String? = null,

    /**
     * Телефон пользователя
     */
    val phone: String? = null,

    /**
     * Пароль пользователя
     */
    val password: String? = null,

    var documents: MutableList<InputPersonDocuments> = mutableListOf()

) {

    var documentsEntity: MutableList<PersonDocuments> = mutableListOf()


    fun getDocsEntity() = documents.map {
        PersonDocuments(
            serial = it.serial,
            number = it.number,
            authority = it.authority,
            dateIssue = it.dateIssue,
            type = it.type,
            personId = this.id
        )
    }

}

