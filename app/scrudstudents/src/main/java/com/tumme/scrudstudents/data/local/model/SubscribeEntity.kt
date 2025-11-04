package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "subscribe_table",
    primaryKeys = ["idStudent", "idCourse"],
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class,
            parentColumns = ["idStudent"],
            childColumns = ["idStudent"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["idCourse"],
            childColumns = ["idCourse"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SubscribeEntity(
    val idStudent: Int,
    val idCourse: Int,
    val score: Float
)