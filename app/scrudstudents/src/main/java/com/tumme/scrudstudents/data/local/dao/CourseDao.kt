package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.CourseWithTeacher
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses ORDER BY nameCourse ASC")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    @Delete
    suspend fun delete(course: CourseEntity)

    @Query("SELECT * FROM courses WHERE idCourse = :id LIMIT 1")
    suspend fun getCourseById(id: Int): CourseEntity?

    @Query("SELECT * FROM courses WHERE teacherId = :teacherId ORDER BY nameCourse ASC")
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE levelCourse = :level ORDER BY nameCourse ASC")
    fun getCoursesByLevel(level: String): Flow<List<CourseEntity>>

    @Transaction
    @Query("""
        SELECT c.*, t.firstName AS teacherFirstName, t.lastName AS teacherLastName
        FROM courses AS c
        LEFT JOIN teachers AS t ON c.teacherId = t.idTeacher
    """)
    fun getAllCoursesWithTeacher(): Flow<List<CourseWithTeacher>>

    @Transaction
    @Query("""
        SELECT c.*, t.firstName AS teacherFirstName, t.lastName AS teacherLastName
        FROM courses AS c
        LEFT JOIN teachers AS t ON c.teacherId = t.idTeacher
        WHERE c.teacherId = :teacherId
    """)
    fun getCoursesWithTeacherByTeacher(teacherId: Int): Flow<List<CourseWithTeacher>>

    @Transaction
    @Query("""
        SELECT c.*, t.firstName AS teacherFirstName, t.lastName AS teacherLastName
        FROM courses AS c
        LEFT JOIN teachers AS t ON c.teacherId = t.idTeacher
        WHERE c.levelCourse = :level
    """)
    fun getCoursesWithTeacherByLevel(level: String): Flow<List<CourseWithTeacher>>
}