package com.tumme.scrudstudents.ui.subscribe

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    studentId: Int? = null,
    teacherId: Int? = null,
    viewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val allStudents by viewModel.allStudents.collectAsState()
    val allCourses by viewModel.allCourses.collectAsState()

    var selectedStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    var score by remember { mutableStateOf("") }

    var isStudentDropdownExpanded by remember { mutableStateOf(false) }
    var isCourseDropdownExpanded by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val isStudentMode = studentId != null
    val isTeacherMode = teacherId != null

    LaunchedEffect(allStudents, studentId) {
        if (isStudentMode) {
            selectedStudent = allStudents.find { it.idStudent == studentId }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = if (isStudentMode) "Enroll in Course" else "Enter Grade"
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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

            if (isTeacherMode) {
                ExposedDropdownMenuBox(
                    expanded = isStudentDropdownExpanded,
                    onExpandedChange = { isStudentDropdownExpanded = !isStudentDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStudent?.let { "${it.lastName} ${it.firstName}" } ?: "Select Student",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Student") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStudentDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = isStudentDropdownExpanded,
                        onDismissRequest = { isStudentDropdownExpanded = false }
                    ) {
                        allStudents.forEach { student ->
                            DropdownMenuItem(
                                text = { Text("${student.lastName} ${student.firstName}") },
                                onClick = {
                                    selectedStudent = student
                                    isStudentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = selectedStudent?.let { "${it.lastName} ${it.firstName}" } ?: "Loading...",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Student") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            ExposedDropdownMenuBox(
                expanded = isCourseDropdownExpanded,
                onExpandedChange = { isCourseDropdownExpanded = !isCourseDropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedCourse?.nameCourse ?: "Select Course",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Course") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCourseDropdownExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isCourseDropdownExpanded,
                    onDismissRequest = { isCourseDropdownExpanded = false }
                ) {
                    allCourses.forEach { course ->
                        DropdownMenuItem(
                            text = { Text(course.nameCourse) },
                            onClick = {
                                selectedCourse = course
                                isCourseDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = score,
                onValueChange = { score = it },
                label = { Text(if (isStudentMode) "Score (leave empty)" else "Score (e.g., 15.5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                readOnly = isStudentMode
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val scoreFloat = if (isStudentMode) 0f else score.toFloatOrNull()

                    if (selectedStudent == null || selectedCourse == null || scoreFloat == null) {
                        Toast.makeText(context, "Student and Course are required", Toast.LENGTH_SHORT).show()
                    } else {
                        viewModel.addSubscription(
                            studentId = selectedStudent!!.idStudent,
                            courseId = selectedCourse!!.idCourse,
                            score = scoreFloat
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isStudentMode) "ENROLL" else "SAVE GRADE")
            }
        }
    }
}