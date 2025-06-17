package io.diplom.outer.images

import io.diplom.config.minio.MinioClient
import io.diplom.config.minio.MinioConfiguration
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class FileService(
    val client: MinioClient
) {
    @ApplicationScoped
    fun minio() = MinioService(client, MinioConfiguration.DOCS)

}
