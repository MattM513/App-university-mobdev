package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscribeViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    val subscriptions = repo.getSubscriptionDetails()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allStudents = repo.getAllStudents()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val allCourses = repo.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addSubscription(studentId: Int, courseId: Int, score: Float) = viewModelScope.launch {
        val newSubscription = SubscribeEntity(studentId, courseId, score)
        repo.insertSubscribe(newSubscription)
    }

    fun deleteSubscription(studentId: Int, courseId: Int) = viewModelScope.launch {
        val subscriptionToDelete = SubscribeEntity(studentId, courseId, 0f)
        repo.deleteSubscribe(subscriptionToDelete)
    }
}

private fun SCRUDRepository.getSubscriptionDetails() {
}
