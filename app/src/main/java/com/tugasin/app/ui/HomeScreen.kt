package com.tugasin.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tugasin.app.data.TaskRepository
import com.tugasin.app.model.Priority
import com.tugasin.app.ui.components.FilterSortSheet
import com.tugasin.app.ui.components.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    repository: TaskRepository,
    onNavigateToAdd: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val allTasks by repository.tasks.collectAsState()

    var selectedPriority by remember { mutableStateOf<Priority?>(null) }
    var selectedCompleted by remember { mutableStateOf<Boolean?>(null) }
    var sortByPriority by remember { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val displayedTasks = remember(allTasks, selectedPriority, selectedCompleted, sortByPriority) {
        repository.getFilteredAndSortedTasks(selectedPriority, selectedCompleted, sortByPriority)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tugasin",
                        modifier = Modifier.semantics { contentDescription = "app_title" }
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier.semantics { contentDescription = "btn_filter" }
                    ) {
                        Icon(Icons.Default.List, contentDescription = "Filter & Sort")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAdd,
                modifier = Modifier.semantics { contentDescription = "btn_add_task" }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->

        if (displayedTasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada tugas. klik + untuk menambahkan tugas!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.semantics { contentDescription = "txt_empty_state" }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .semantics { contentDescription = "task_list" },
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(displayedTasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggleComplete = { repository.toggleTaskCompletion(it) },
                        onEdit = onNavigateToEdit,
                        onDelete = { repository.deleteTask(it) }
                    )
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterSortSheet(
            selectedPriority = selectedPriority,
            selectedCompleted = selectedCompleted,
            sortByPriority = sortByPriority,
            onPrioritySelected = { selectedPriority = it },
            onCompletedSelected = { selectedCompleted = it },
            onSortByPriorityChanged = { sortByPriority = it },
            onDismiss = { showFilterSheet = false }
        )
    }
}