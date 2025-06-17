package io.diplom.works.dto.inp

import io.vertx.ext.web.FileUpload

data class AttachImagesWorkDTO(
    val images: List<FileUpload>,
    val workId: Long
)
