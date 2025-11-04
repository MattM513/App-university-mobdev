package com.tumme.scrudstudents.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    val teachers = repo.getAllTeachers()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    fun insertTeacher(teacher: TeacherEntity) = viewModelScope.launch {
        repo.insertTeacher(teacher)
        _events.emit("Teacher saved")
    }

    fun deleteTeacher(teacher: TeacherEntity) = viewModelScope.launch {
        repo.deleteTeacher(teacher)
        _events.emit("Teacher deleted")
    }
}