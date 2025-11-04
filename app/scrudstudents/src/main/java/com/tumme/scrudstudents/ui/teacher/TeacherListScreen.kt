package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherListScreen(
    viewModel: TeacherViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val teachers by viewModel.teachers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Teachers") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) { Text("+") }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TableHeader(
                cells = listOf("ID", "Last Name", "First Name", "Email", "Actions"),
                weights = listOf(0.1f, 0.25f, 0.25f, 0.25f, 0.15f)
            )
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(teachers) { teacher ->
                    TeacherRow(
                        teacher = teacher,
                        onDelete = { viewModel.deleteTeacher(teacher) }
                    )
                }
            }
        }
    }
}

@Composable
fun TeacherRow(teacher: TeacherEntity, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(teacher.idTeacher.toString(), modifier = Modifier.weight(0.1f))
        Text(
            teacher.lastName,
            modifier = Modifier.weight(0.25f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            teacher.firstName,
            modifier = Modifier.weight(0.25f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            teacher.email,
            modifier = Modifier.weight(0.25f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Box(
            modifier = Modifier.weight(0.15f),
            contentAlignment = Alignment.CenterEnd
        ) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}