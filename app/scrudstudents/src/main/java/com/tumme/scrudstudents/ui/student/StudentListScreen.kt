package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToCourses: () -> Unit = {},
    onNavigateToSubscribes: () -> Unit = {},
    isAdmin: Boolean = false
) {
    val students by viewModel.students.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isAdmin) "Manage Students" else "Students") },
                navigationIcon = {
                    if (isAdmin) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                },
                actions = {
                    if (!isAdmin) {
                        IconButton(onClick = onNavigateToCourses) {
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
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            TableHeader(cells = listOf("DOB","Last", "First", "Gender", "Actions"),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student ->
                    StudentRow(
                        student = student,
                        onEdit = { },
                        onDelete = { viewModel.deleteStudent(student) },
                        onView = { onNavigateToDetail(student.idStudent) },
                        onShare = { }
                    )
                }
            }
        }
    }
}