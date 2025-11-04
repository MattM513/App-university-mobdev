package com.tumme.scrudstudents.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    onManageStudents: () -> Unit,
    onManageTeachers: () -> Unit,
    onManageCourses: () -> Unit,
    onManageSubscriptions: () -> Unit,
    onLogout: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Admin Dashboard") }) }
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
                text = "Welcome, Admin",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Button(onClick = onManageStudents, modifier = Modifier.fillMaxWidth()) {
                Text("Manage Students")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onManageTeachers, modifier = Modifier.fillMaxWidth()) {
                Text("Manage Teachers")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onManageCourses, modifier = Modifier.fillMaxWidth()) {
                Text("Manage Courses")
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onManageSubscriptions, modifier = Modifier.fillMaxWidth()) {
                Text("Manage Subscriptions / Grades")
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