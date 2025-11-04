package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    teacherId: Int,
    onNavigateToCourses: () -> Unit,
    onNavigateToGradeEntry: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Teacher Dashboard") }) }
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
                text = "Welcome, Teacher",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(
                onClick = onNavigateToCourses,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Manage My Courses")
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onNavigateToGradeEntry,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enter Student Grades")
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