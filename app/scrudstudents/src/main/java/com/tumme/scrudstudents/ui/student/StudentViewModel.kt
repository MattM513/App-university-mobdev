package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FinalGradeState(
    val average: Double = 0.0,
    val totalEcts: Float = 0f,
    val details: String = ""
)

@HiltViewModel
class StudentViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _student = MutableStateFlow<StudentEntity?>(null)
    val student = _student.asStateFlow()

    private val _finalGrade = MutableStateFlow(FinalGradeState())
    val finalGrade = _finalGrade.asStateFlow()

    fun loadStudent(studentId: Int) {
        viewModelScope.launch {
            _student.value = repo.getStudentById(studentId)
        }
    }

    fun calculateFinalGrade(studentId: Int) {
        viewModelScope.launch {
            repo.getSubscriptionsWithCourseByStudent(studentId)
                .map { subscriptions ->
                    if (subscriptions.isEmpty()) {
                        return@map FinalGradeState(0.0, 0f, "No grades found.")
                    }

                    var totalScoreEcts: Double = 0.0
                    var totalEcts: Float = 0f
                    val details = StringBuilder()

                    subscriptions.forEach { subWithCourse ->
                        val score = subWithCourse.subscription.score
                        val ects = subWithCourse.ectsCourse
                        totalScoreEcts += score * ects
                        totalEcts += ects
                        details.append("${subWithCourse.nameCourse} (Score: $score, ECTS: $ects)\n")
                    }

                    val average = if (totalEcts > 0) totalScoreEcts / totalEcts else 0.0
                    FinalGradeState(average, totalEcts, details.toString())
                }
                .collect {
                    _finalGrade.value = it
                }
        }
    }
}