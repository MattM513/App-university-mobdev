package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentFormScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onSaved: ()->Unit = {}
) {
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var dobText by remember { mutableStateOf("2000-01-01") }
    var gender by remember { mutableStateOf(Gender.NotConcerned) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val levels = listOf("P1", "P2", "P3", "B1", "B2", "B3", "A1", "A2", "A3", "MS", "PhD")
    var level by remember { mutableStateOf(levels[0]) }
    var isLevelDropdownExpanded by remember { mutableStateOf(false) }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
            value = dobText,
            onValueChange = { dobText = it },
            label = { Text("Date of birth (yyyy-MM-dd)") },
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
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(
                        selected = (g == gender),
                        onClick = { gender = g }
                    )
                    Text(text = g.value, modifier = Modifier.padding(start = 4.dp, end = 8.dp))
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
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
                viewModel.insertStudent(student)
                onSaved()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}