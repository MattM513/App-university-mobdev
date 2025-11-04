package com.tumme.scrudstudents.ui.course

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFormScreen(
    teacherId: Int?,
    isAdmin: Boolean = false,
    courseViewModel: CourseListViewModel = hiltViewModel(),
    teacherViewModel: TeacherViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var ects by remember { mutableStateOf("") }

    val levels = listOf("P1", "P2", "P3", "B1", "B2", "B3", "A1", "A2", "A3", "MS", "PhD")
    var level by remember { mutableStateOf(levels[0]) }
    var isLevelDropdownExpanded by remember { mutableStateOf(false) }

    val teachers by teacherViewModel.teachers.collectAsState()
    var selectedTeacher by remember { mutableStateOf<TeacherEntity?>(null) }
    var isTeacherDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(teachers, teacherId, isAdmin) {
        if (!isAdmin && teacherId != null) {
            selectedTeacher = teachers.find { it.idTeacher == teacherId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Course") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Course Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ects,
                onValueChange = { ects = it },
                label = { Text("ECTS Credits") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            ExposedDropdownMenuBox(
                expanded = isLevelDropdownExpanded,
                onExpandedChange = { isLevelDropdownExpanded = !isLevelDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = level,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Level") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isLevelDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isLevelDropdownExpanded,
                    onDismissRequest = { isLevelDropdownExpanded = false }
                ) {
                    levels.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                level = item
                                isLevelDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            if (isAdmin) {
                ExposedDropdownMenuBox(
                    expanded = isTeacherDropdownExpanded,
                    onExpandedChange = { isTeacherDropdownExpanded = !isTeacherDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedTeacher?.let { "${it.lastName} ${it.firstName}" } ?: "Select Teacher (Optional)",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Teacher") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTeacherDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isTeacherDropdownExpanded,
                        onDismissRequest = { isTeacherDropdownExpanded = false }
                    ) {
                        teachers.forEach { teacher ->
                            DropdownMenuItem(
                                text = { Text("${teacher.lastName} ${teacher.firstName}") },
                                onClick = {
                                    selectedTeacher = teacher
                                    isTeacherDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val ectsFloat = ects.toFloatOrNull()
                    val finalTeacherId = selectedTeacher?.idTeacher ?: teacherId

                    if (name.isBlank() || ectsFloat == null || ectsFloat <= 0) {
                        Toast.makeText(
                            context,
                            "Validation Error: Name cannot be empty and ECTS must be > 0",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        val newCourse = CourseEntity(
                            nameCourse = name.trim(),
                            ectsCourse = ectsFloat,
                            levelCourse = level,
                            teacherId = if (isAdmin) selectedTeacher?.idTeacher else teacherId
                        )
                        courseViewModel.insertCourse(newCourse)
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("SAVE COURSE")
            }
        }
    }
}