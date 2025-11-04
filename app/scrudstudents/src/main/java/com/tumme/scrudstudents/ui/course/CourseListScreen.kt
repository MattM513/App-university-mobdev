package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseWithTeacher
import com.tumme.scrudstudents.ui.components.TableHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    teacherId: Int? = null,
    filterLevel: String? = null,
    isAdmin: Boolean = false,
    viewModel: CourseListViewModel = hiltViewModel(),
    onNavigateToForm: (() -> Unit)? = null,
    onNavigateBack: () -> Unit
) {
    val courses by viewModel.courses.collectAsState()

    LaunchedEffect(teacherId, filterLevel, isAdmin) {
        if (isAdmin) {
            viewModel.loadCourses()
        } else {
            viewModel.loadCourses(teacherId, filterLevel)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isAdmin) "Manage Courses" else "Courses") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                })
        },
        floatingActionButton = {
            if (onNavigateToForm != null) {
                FloatingActionButton(onClick = onNavigateToForm) { Text("+") }
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {

            TableHeader(
                cells = listOf("Name", "Teacher", "ECTS", "Level", "Actions"),
                weights = listOf(0.3f, 0.2f, 0.15f, 0.15f, 0.2f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { courseWithTeacher ->
                    CourseRow(
                        courseWithTeacher = courseWithTeacher,
                        onDelete = {
                            viewModel.deleteCourse(courseWithTeacher.course)
                        },
                        showDelete = (teacherId != null || isAdmin)
                    )
                }
            }
        }
    }
}

@Composable
fun CourseRow(courseWithTeacher: CourseWithTeacher, onDelete: () -> Unit, showDelete: Boolean) {
    val course = courseWithTeacher.course
    val teacherName = "${courseWithTeacher.teacherFirstName ?: ""} ${courseWithTeacher.teacherLastName ?: "N/A"}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = course.nameCourse,
            modifier = Modifier.weight(0.3f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = teacherName,
            modifier = Modifier.weight(0.2f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = course.ectsCourse.toString(),
            modifier = Modifier.weight(0.15f)
        )
        Text(
            text = course.levelCourse,
            modifier = Modifier.weight(0.15f)
        )
        Box(
            modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (showDelete) {
                IconButton(onClick = onDelete) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Course")
                }
            }
        }
    }
}