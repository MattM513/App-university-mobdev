package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.*
import kotlinx.coroutines.flow.Flow

class SCRUDRepository(
    private val studentDao: StudentDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao,
    private val teacherDao: TeacherDao
) {
    // Students
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    // Courses
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    // Subscribes
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)

    fun getSubscriptionDetails(): Flow<List<SubscriptionDetails>> = subscribeDao.getSubscriptionDetails()

    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)
    suspend fun getTeacherByEmail(email: String) = teacherDao.getTeacherByEmail(email)
}