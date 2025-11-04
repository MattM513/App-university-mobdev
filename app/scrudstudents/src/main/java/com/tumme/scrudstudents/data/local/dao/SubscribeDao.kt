package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.SubscriptionDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscribeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscribe: SubscribeEntity)

    @Delete
    suspend fun delete(subscribe: SubscribeEntity)

    @Query("SELECT * FROM subscribe_table")
    fun getAllSubscribes(): Flow<List<SubscribeEntity>>

    @Query("""
        SELECT 
            s.idStudent, c.idCourse, 
            s.firstName AS studentFirstName, s.lastName AS studentLastName, 
            c.nameCourse AS courseName, sub.score
        FROM subscribe_table AS sub
        JOIN students AS s ON sub.idStudent = s.idStudent
        JOIN courses AS c ON sub.idCourse = c.idCourse
        ORDER BY s.lastName, c.nameCourse
    """)
    fun getSubscriptionDetails(): Flow<List<SubscriptionDetails>>

    @Query("SELECT * FROM subscribe_table WHERE idStudent = :sId")
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>>

    @Query("SELECT * FROM subscribe_table WHERE idCourse = :cId")
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>>
}