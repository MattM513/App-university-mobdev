package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.SubscriptionDetails
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _subscriptions = MutableStateFlow<List<SubscriptionDetails>>(emptyList())
    val subscriptions: StateFlow<List<SubscriptionDetails>> = _subscriptions

    val allStudents: StateFlow<List<StudentEntity>> = repo.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allCourses: StateFlow<List<CourseEntity>> = repo.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun loadSubscriptions(studentId: Int? = null) {
        viewModelScope.launch {
            val flow = if (studentId != null) {
                repo.getSubscriptionDetailsByStudent(studentId)
            } else {
                repo.getSubscriptionDetails()
            }

            flow.collect {
                _subscriptions.value = it
            }
        }
    }

    fun addSubscription(studentId: Int, courseId: Int, score: Float) = viewModelScope.launch {
        val newSubscription = SubscribeEntity(studentId, courseId, score)
        repo.insertSubscribe(newSubscription)
    }

    fun deleteSubscription(studentId: Int, courseId: Int) = viewModelScope.launch {
        val subscriptionToDelete = SubscribeEntity(studentId, courseId, 0f)
        repo.deleteSubscribe(subscriptionToDelete)
    }
}