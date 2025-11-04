package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

// tableName = "students" : Définit le nom de la table.
@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val idStudent: Int,
    //idStudent est la clé primaire de la table, qui sera une colonne dans la table

    // Les propriétés suivantes seront
    // automatiquement mappées en colonnes dans la table students.
    val lastName: String,
    val firstName: String,
    val dateOfBirth: Date,
    val gender: Gender
)