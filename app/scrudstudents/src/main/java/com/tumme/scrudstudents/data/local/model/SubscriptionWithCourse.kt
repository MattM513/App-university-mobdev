package com.tumme.scrudstudents.data.local.model

import androidx.room.Embedded

data class SubscriptionWithCourse(
    @Embedded
    val subscription: SubscribeEntity,
    val nameCourse: String,
    val ectsCourse: Float
)