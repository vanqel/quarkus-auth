package io.diplom.dto.inp

import io.diplom.models.PersonDocuments
import java.time.LocalDate

class InputPersonDocuments(

    var id: Long? = null,

    val serial: String? = null,

    val number: String? = null,

    val authority: String? = null,

    val dateIssue: LocalDate? = null,

    val type: PersonDocuments.DocType? = null

)
