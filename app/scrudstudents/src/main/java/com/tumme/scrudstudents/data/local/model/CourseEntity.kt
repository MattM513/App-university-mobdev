package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey(autoGenerate = true)
    val idCourse: Int = 0,
    val nameCourse: String,
    val ectsCourse: Float,
    val levelCourse: String
)