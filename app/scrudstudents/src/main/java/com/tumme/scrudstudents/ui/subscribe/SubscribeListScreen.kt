package com.tumme.scrudstudents.ui.subscribe

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscriptionDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    viewModel: SubscribeViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val subscriptions by viewModel.subscriptions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Subscriptions") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Student", modifier = Modifier.weight(0.4f), fontWeight = FontWeight.Bold)
                    Text("Course", modifier = Modifier.weight(0.4f), fontWeight = FontWeight.Bold)
                    Text("Score", modifier = Modifier.weight(0.2f), fontWeight = FontWeight.Bold)
                }
                HorizontalDivider()
            }
            items(subscriptions) { sub ->
                SubscriptionRow(
                    details = sub,
                    onDelete = {
                        viewModel.deleteSubscription(sub.idStudent, sub.idCourse)
                    }
                )
            }
        }
    }
}

@Composable
fun SubscriptionRow(details: SubscriptionDetails, onDelete: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${details.studentLastName.uppercase()} ${details.studentFirstName}",
            modifier = Modifier.weight(0.4f),
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        Text(
            text = details.courseName,
            modifier = Modifier.weight(0.4f),
            maxLines = 1, overflow = TextOverflow.Ellipsis
        )
        Text(
            text = details.score.toString(),
            modifier = Modifier.weight(0.2f)
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, "Delete")
        }
    }
}