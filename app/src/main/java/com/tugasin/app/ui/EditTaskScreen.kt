package com.tugasin.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.tugasin.app.data.TaskRepository
import com.tugasin.app.model.TaskCategory
import com.tugasin.app.util.PriorityHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    repository: TaskRepository,
    onNavigateBack: () -> Unit
) {
    val task = remember { repository.getTaskById(taskId) }

    if (task == null) {
        onNavigateBack()
        return
    }

    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }
    var selectedCategory by remember { mutableStateOf(task.category) }
    var selectedDeadline by remember { mutableStateOf(task.deadline) }
    var titleError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = task.deadline
    )
    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    val calculatedPriority = remember(selectedCategory) {
        PriorityHelper.calculatePriority(selectedCategory)
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDeadline = datePickerState.selectedDateMillis
                        showDatePicker = false
                    },
                    modifier = Modifier.semantics { contentDescription = "btn_confirm_date" }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    modifier = Modifier.semantics { contentDescription = "btn_cancel_date" }
                ) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Task") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.semantics { contentDescription = "btn_back" }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input judul
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = { Text("Judul Task") },
                isError = titleError,
                supportingText = { if (titleError) Text("Judul tidak boleh kosong") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "input_task_title" },
                singleLine = true
            )

            // Input deskripsi
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi (opsional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "input_task_description" },
                minLines = 2
            )

            // Pilih kategori
            Text(text = "Kategori Tugas", style = MaterialTheme.typography.titleSmall)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TaskCategory.entries.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics { contentDescription = "radio_category_${category.name.lowercase()}" }
                    ) {
                        RadioButton(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Pilih deadline
            Text(text = "Deadline", style = MaterialTheme.typography.titleSmall)
            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "btn_pick_deadline" },
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = if (selectedDeadline != null)
                        dateFormatter.format(Date(selectedDeadline!!))
                    else
                        "Pilih Deadline"
                )
            }

            // Preview priority otomatis
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "card_priority_preview" },
                colors = CardDefaults.cardColors(
                    containerColor = PriorityHelper.getColor(calculatedPriority).copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Priority Otomatis",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Skor: ${
                                PriorityHelper.getCategoryScore(selectedCategory)
                            } (kategori: ${PriorityHelper.getCategoryScore(selectedCategory)})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Badge(
                        containerColor = PriorityHelper.getColor(calculatedPriority),
                        modifier = Modifier.semantics { contentDescription = "badge_calculated_priority" }
                    ) {
                        Text(
                            text = PriorityHelper.getLabel(calculatedPriority),
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val isTitleEmpty = title.isBlank()

                    titleError = isTitleEmpty

                    if (!isTitleEmpty) {
                        repository.updateTask(
                            task.copy(
                                title = title.trim(),
                                description = description.trim(),
                                category = selectedCategory,
                                deadline = selectedDeadline,
                                priority = calculatedPriority
                            )
                        )
                        onNavigateBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = "btn_update_task" }
            ) {
                Text("Perbarui Tugas")
            }
        }
    }
}