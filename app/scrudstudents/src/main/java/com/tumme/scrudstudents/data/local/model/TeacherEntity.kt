package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    val idTeacher: Int = 0,
    val lastName: String,
    val firstName: String,

    val email: String,
    val password: String

    )