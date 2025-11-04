package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    studentId: Int,
    viewModel: StudentViewModel = hiltViewModel(),
    onNavigateToCourses: (String) -> Unit,
    onNavigateToEnroll: () -> Unit,
    onNavigateToMyGrades: () -> Unit,
    onNavigateToFinalGrade: () -> Unit,
    onLogout: () -> Unit
) {
    val student by viewModel.student.collectAsState()

    LaunchedEffect(studentId) {
        viewModel.loadStudent(studentId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Student Dashboard") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome, ${student?.firstName ?: "..."}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Level: ${student?.level ?: "..."}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = { student?.level?.let { onNavigateToCourses(it) } },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("See Courses for My Level")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onNavigateToEnroll,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enroll in a Course")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onNavigateToMyGrades,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Grades")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onNavigateToFinalGrade,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Final Grade Summary")
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Logout")
            }
        }
    }
}