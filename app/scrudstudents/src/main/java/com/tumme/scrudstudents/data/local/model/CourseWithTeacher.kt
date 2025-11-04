package com.tumme.scrudstudents.data.local.model

import androidx.room.Embedded

data class CourseWithTeacher(
    @Embedded
    val course: CourseEntity,
    val teacherFirstName: String?,
    val teacherLastName: String?
)