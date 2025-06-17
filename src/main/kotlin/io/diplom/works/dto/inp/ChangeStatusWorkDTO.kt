package io.diplom.works.dto.inp

import io.diplom.works.models.WorkEntity

data class ChangeStatusWorkDTO(
    val id: Long,
    val status: WorkEntity.Status
)
