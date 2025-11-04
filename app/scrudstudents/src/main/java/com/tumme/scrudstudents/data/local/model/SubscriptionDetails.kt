package com.tumme.scrudstudents.data.local.model

data class SubscriptionDetails(
    val idStudent: Int,
    val idCourse: Int,
    val studentFirstName: String,
    val studentLastName: String,
    val courseName: String,
    val score: Float
)