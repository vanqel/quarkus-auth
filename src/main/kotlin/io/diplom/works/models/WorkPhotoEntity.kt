package io.diplom.works.models

import io.diplom.models.LongEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "work_photo")
class WorkPhotoEntity(

    @Column(name = "work_id")
    val workId: Long? = null,

    @Column(name = "filename")
    val filename: String? = null

) : LongEntity() {

    @Transient
    var uri: String? = null

}
