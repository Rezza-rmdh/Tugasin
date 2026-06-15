package com.tugasin.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.tugasin.app.data.TaskRepository
import com.tugasin.app.model.Task
import com.tugasin.app.model.TaskCategory
import com.tugasin.app.util.PriorityHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    repository: TaskRepository,
    onNavigateBack: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(TaskCategory.DAILY) }
    var selectedDeadline by remember { mutableStateOf<Long?>(null) }
    var titleError by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
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
                    modifier = Modifier.testTag("btn_confirm_date")
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false },
                    modifier = Modifier.testTag("btn_cancel_date")
                ) { Text("Batal") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Task") },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("btn_back")
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
            OutlinedTextField(
                value = title,
                onValueChange = {
                    title = it
                    titleError = false
                },
                label = { Text("Judul Task") },
                isError = titleError,
                supportingText = {
                    if (titleError) Text(
                        text = "Judul tidak boleh kosong",
                        modifier = Modifier.testTag("txt_title_error")
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_task_title"),
                singleLine = true
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Deskripsi (opsional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_task_description"),
                minLines = 2
            )

            Text(text = "Kategori Tugas", style = MaterialTheme.typography.titleSmall)

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                TaskCategory.entries.forEach { category ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("radio_category_${category.name.lowercase()}")
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

            Text(text = "Deadline (opsional)", style = MaterialTheme.typography.titleSmall)

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_pick_deadline")
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

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_priority_preview"),
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
                            text = "Skor kategori: ${PriorityHelper.getCategoryScore(selectedCategory)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Badge(
                        containerColor = PriorityHelper.getColor(calculatedPriority),
                        modifier = Modifier.testTag("badge_calculated_priority")
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
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        repository.addTask(
                            Task(
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
                    .testTag("btn_save_task")
            ) {
                Text("Simpan Task")
            }
        }
    }
}