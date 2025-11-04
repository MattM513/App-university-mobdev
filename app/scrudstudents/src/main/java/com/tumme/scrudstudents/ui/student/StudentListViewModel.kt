package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    // 1. repo.getAllStudents() : On récupère le 'Flow' depuis le repository.
    // 2. .stateIn(...) : On transforme ce "Flow" froid en un "StateFlow" chaud.
    //    - 'viewModelScope' : La coroutine qui collecte ce flux
    //      vivra aussi longtemps que le ViewModel.
    //    - 'SharingStarted.Lazily' : La collecte commence
    //      seulement quand l'UI (le Screen) commence à observer ce StateFlow.
    //    - 'emptyList()' : La valeur initiale, avant que la DB ne renvoie des données.
    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // On expose une version 'read-only' (non-mutable)
    // du StateFlow à l'UI. L'UI ne pourra pas "modifier" cette liste,
    // elle ne pourra que l'observer.
    val students: StateFlow<List<StudentEntity>> = _students

    // Un 'SharedFlow' est utilisé pour envoyer des "événements"
    // uniques à l'UI (ex: un message Toast ou Snackbar).
    // Contrairement à StateFlow, il n'a pas de "valeur actuelle".
    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow() // Version read-only pour l'UI

    // Fonction appelée par l'UI pour supprimer un étudiant.
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        //  'viewModelScope.launch' : Lance une coroutine attachée au cycle de vie du ViewModel.

        // On appelle la fonction 'suspend' du repository
        repo.deleteStudent(student)

        // On émet un événement pour que l'UI puisse afficher un message.
        _events.emit("Student deleted")
    }

    // Fonction pour insérer
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        repo.insertStudent(student)
        _events.emit("Student inserted")
    }

    // Fonction 'suspend' pour trouver un étudiant
    suspend fun findStudent(id: Int) = repo.getStudentById(id)
}