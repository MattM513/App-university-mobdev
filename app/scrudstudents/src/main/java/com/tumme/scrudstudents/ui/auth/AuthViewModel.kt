package com.tumme.scrudstudents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class UserRole { STUDENT, TEACHER, ADMIN, NONE }

data class AuthState(
    val isAuthenticated: Boolean = false,
    val role: UserRole = UserRole.NONE,
    val userId: Int = -1,
    val error: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch {
            if (email.trim() == "admin@admin.com" && password == "admin") {
                _authState.value = AuthState(
                    isAuthenticated = true,
                    role = UserRole.ADMIN,
                    userId = 0
                )
                return@launch
            }

            val student = repo.getStudentByEmail(email)
            if (student != null && student.password == password) {
                _authState.value = AuthState(
                    isAuthenticated = true,
                    role = UserRole.STUDENT,
                    userId = student.idStudent
                )
                return@launch
            }

            val teacher = repo.getTeacherByEmail(email)
            if (teacher != null && teacher.password == password) {
                _authState.value = AuthState(
                    isAuthenticated = true,
                    role = UserRole.TEACHER,
                    userId = teacher.idTeacher
                )
                return@launch
            }

            _authState.value = AuthState(error = "Invalid credentials")
        }
    }

    fun registerStudent(student: StudentEntity) {
        viewModelScope.launch {
            repo.insertStudent(student)
            _authState.value = AuthState(
                isAuthenticated = true,
                role = UserRole.STUDENT,
                userId = student.idStudent
            )
        }
    }

    fun registerTeacher(teacher: TeacherEntity) {
        viewModelScope.launch {
            repo.insertTeacher(teacher)
            _authState.value = AuthState(
                isAuthenticated = true,
                role = UserRole.TEACHER,
                userId = teacher.idTeacher
            )
        }
    }

    fun logout() {
        _authState.value = AuthState()
    }
}