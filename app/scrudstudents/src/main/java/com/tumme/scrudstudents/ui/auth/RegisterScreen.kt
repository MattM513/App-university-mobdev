package com.tumme.scrudstudents.ui.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onRegisterSuccess: (role: UserRole, id: Int) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

    var dobText by remember { mutableStateOf("2000-01-01") }
    var gender by remember { mutableStateOf(Gender.NotConcerned) }
    val levels = listOf("P1", "P2", "P3", "B1", "B2", "B3", "A1", "A2", "A3", "MS", "PhD")
    var level by remember { mutableStateOf(levels[0]) }
    var isLevelDropdownExpanded by remember { mutableStateOf(false) }

    val authState by authViewModel.authState.collectAsState()
    val context = LocalContext.current
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    LaunchedEffect(authState) {
        if (authState.isAuthenticated) {
            onRegisterSuccess(authState.role, authState.userId)
        }
        authState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Register") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Select Role", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedRole == UserRole.STUDENT,
                    onClick = { selectedRole = UserRole.STUDENT }
                )
                Text("Student", Modifier.padding(end = 16.dp))
                RadioButton(
                    selected = selectedRole == UserRole.TEACHER,
                    onClick = { selectedRole = UserRole.TEACHER }
                )
                Text("Teacher")
            }

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            if (selectedRole == UserRole.STUDENT) {
                OutlinedTextField(
                    value = dobText,
                    onValueChange = { dobText = it },
                    label = { Text("Date of birth (yyyy-MM-dd)") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = isLevelDropdownExpanded,
                    onExpandedChange = { isLevelDropdownExpanded = !isLevelDropdownExpanded },
                    modifier = Modifier.fillMaxWidth()
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

                Row {
                    listOf(Gender.Male, Gender.Female, Gender.NotConcerned).forEach { g->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (g == gender),
                                onClick = { gender = g }
                            )
                            Text(text = g.value, modifier = Modifier.padding(start = 4.dp, end = 8.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank() || lastName.isBlank() || firstName.isBlank()) {
                        Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    if (selectedRole == UserRole.STUDENT) {
                        val dob = dateFormat.parse(dobText) ?: Date()
                        val student = StudentEntity(
                            lastName = lastName,
                            firstName = firstName,
                            dateOfBirth = dob,
                            gender = gender,
                            email = email.trim(),
                            password = password,
                            level = level
                        )
                        authViewModel.registerStudent(student)
                    } else {
                        val teacher = TeacherEntity(
                            lastName = lastName,
                            firstName = firstName,
                            email = email.trim(),
                            password = password
                        )
                        authViewModel.registerTeacher(teacher)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }
            TextButton(onClick = onNavigateBack) {
                Text("Already have an account? Login")
            }
        }
    }
}