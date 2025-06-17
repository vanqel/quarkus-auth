package io.diplom.works.dto.out

import io.diplom.common.security.models.User
import io.diplom.works.models.WorkEntity

data class UserWorksOutputDTO (
    val user: User,
    val works: List<WorkEntity>
)
