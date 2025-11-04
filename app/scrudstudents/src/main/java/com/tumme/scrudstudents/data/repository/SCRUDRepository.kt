package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class SCRUDRepository(
    private val studentDao: StudentDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao,
    private val teacherDao: TeacherDao
) {
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)
    suspend fun getStudentByEmail(email: String) = studentDao.getStudentByEmail(email)

    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    fun getCoursesWithTeacherByLevel(level: String): Flow<List<CourseWithTeacher>> {
        return courseDao.getCoursesWithTeacherByLevel(level)
    }

    fun getCoursesWithTeacherByTeacher(teacherId: Int): Flow<List<CourseWithTeacher>> {
        return courseDao.getCoursesWithTeacherByTeacher(teacherId)
    }

    fun getAllCoursesWithTeacher(): Flow<List<CourseWithTeacher>> {
        return courseDao.getAllCoursesWithTeacher()
    }

    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)

    fun getSubscriptionDetails(): Flow<List<SubscriptionDetails>> = subscribeDao.getSubscriptionDetails()
    fun getSubscriptionDetailsByStudent(studentId: Int): Flow<List<SubscriptionDetails>> {
        return subscribeDao.getSubscriptionDetailsByStudent(studentId)
    }

    fun getSubscriptionsWithCourseByStudent(studentId: Int): Flow<List<SubscriptionWithCourse>> {
        return subscribeDao.getSubscriptionsWithCourseByStudent(studentId)
    }

    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)
    suspend fun getTeacherByEmail(email: String) = teacherDao.getTeacherByEmail(email)
}