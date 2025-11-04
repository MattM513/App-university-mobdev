package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable // Annotation qui marque cette fonction comme un composant UI Compose.
fun StudentListScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToCourses: () -> Unit = {},
    onNavigateToSubscribes: () -> Unit = {}
) {
    // 'viewModel.students' : On accède au StateFlow du ViewModel.
    // '.collectAsState()' : On s'abonne à ce StateFlow.
    // 'val students by ...' : 'students' devient une variable d'état Compose.

    // 1. L'utilisateur clique sur "Supprimer".
    // 2. 'viewModel.deleteStudent(student)' est appelée.
    // 3. Le repo supprime l'étudiant de la DB.
    // 4. Le 'Flow' du DAO (getAllStudents) émet une nouvelle liste (sans l'étudiant).
    // 5. Le 'StateFlow' du ViewModel se met à jour avec cette nouvelle liste.
    // 6. '.collectAsState()' détecte ce changement.
    // 7. Compose *recompose* automatiquement tous les composants
    //    qui lisent la variable 'students' (ici, le LazyColumn).
    val students by viewModel.students.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Students") },
                actions = {
                    IconButton(onClick = onNavigateToCourses) { // Appelle la lambda
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Go to Courses"
                        )
                    }
                    IconButton(onClick = onNavigateToSubscribes) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Assignment,
                            contentDescription = "Go to Subscriptions"
                        )
                    }
                })
        },

        floatingActionButton = {
            // Le bouton "+" qui déclenche la navigation vers le formulaire.
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        }
    ) { padding -> // 'padding' contient l'espace pris par la TopBar
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            TableHeader(cells = listOf("DOB","Last", "First", "Gender", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f))

            Spacer(modifier = Modifier.height(8.dp))

            // 'LazyColumn' : Affiche une liste de manière performante (comme RecyclerView).
            LazyColumn(modifier = Modifier.fillMaxSize()) {

                // 'items(students)' : C'est ici que l'état 'students' est lu.
                items(students) { student ->
                    StudentRow(
                        student = student,
                        onEdit = { /* ... */ },
                        onDelete = { viewModel.deleteStudent(student) },
                        onView = { onNavigateToDetail(student.idStudent) },
                        onShare = { /* ... */ }
                    )
                }
            }
        }
    }
}