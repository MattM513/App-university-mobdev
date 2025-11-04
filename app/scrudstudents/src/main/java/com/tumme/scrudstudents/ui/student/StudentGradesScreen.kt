package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    studentId: Int,
    viewModel: StudentViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val gradeState by viewModel.finalGrade.collectAsState()
    val decFormat = DecimalFormat("#.##")

    LaunchedEffect(studentId) {
        viewModel.calculateFinalGrade(studentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Final Grade") },
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
                .padding(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Final Weighted Average:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = decFormat.format(gradeState.average),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Text(
                        text = "Based on ${gradeState.totalEcts} total ECTS credits.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Calculation Details:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = gradeState.details.ifEmpty { "Loading details..." },
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 20.sp
            )
        }
    }
}