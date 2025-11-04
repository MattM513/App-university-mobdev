package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.CourseWithTeacher
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _courses = MutableStateFlow<List<CourseWithTeacher>>(emptyList())
    val courses: StateFlow<List<CourseWithTeacher>> = _courses

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun loadCourses(teacherId: Int? = null, level: String? = null) {
        viewModelScope.launch {
            val flow = when {
                teacherId != null -> repo.getCoursesWithTeacherByTeacher(teacherId)
                level != null -> repo.getCoursesWithTeacherByLevel(level)
                else -> repo.getAllCoursesWithTeacher()
            }

            flow.collect {
                _courses.value = it
            }
        }
    }

    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        repo.deleteCourse(course)
        _events.emit("Course deleted")
    }

    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        repo.insertCourse(course)
        _events.emit("Course inserted")
    }

    suspend fun findCourse(id: Int) = repo.getCourseById(id)
}