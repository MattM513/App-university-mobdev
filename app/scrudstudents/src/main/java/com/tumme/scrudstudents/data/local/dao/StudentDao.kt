package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    //@Query Permet d'écrire des requêtes SQL personnalisées.
    // Cette fonction récupère tous les étudiants, triés.
    @Query("SELECT * FROM students ORDER BY lastName, firstName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    // @Insert : Annotation pour une fonction d'insertion.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    // @Delete : Annotation pour une fonction de suppression.
    // 'suspend' : Également une opération asynchrone.
    @Delete
    suspend fun delete(student: StudentEntity)

    // Une autre requête pour trouver un étudiant par son ID.
    // ':id' est un paramètre lié.
    // 'suspend' car c'est une opération "one-shot" (qui s'exécute une fois).
    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1")
    suspend fun getStudentById(id: Int): StudentEntity?
}