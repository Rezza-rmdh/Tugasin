package com.tugasin.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tugasin.app.model.Task
import com.tugasin.app.util.PriorityHelper

@Composable
fun TaskItem(
    task: Task,
    onToggleComplete: (Int) -> Unit,
    onEdit: (Int) -> Unit,
    onDelete: (Int) -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Hapus Task",
                    modifier = Modifier.testTag("dialog_delete_title")
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin menghapus \"${task.title}\"?",
                    modifier = Modifier.testTag("dialog_delete_message")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(task.id)
                        showDeleteDialog = false
                    },
                    modifier = Modifier.testTag("btn_confirm_delete")
                ) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    modifier = Modifier.testTag("btn_cancel_delete")
                ) {
                    Text("Batal")
                }
            },
            modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("dialog_delete_confirmation")
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("task_item_${task.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleComplete(task.id) },
                modifier = Modifier.testTag("checkbox_task_${task.id}")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                    modifier = Modifier.testTag("task_title_${task.id}")
                )
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("task_description_${task.id}")
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = task.category.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag("task_category_${task.id}")
                )
                Text(
                    text = PriorityHelper.getDeadlineLabel(task.deadline),
                    style = MaterialTheme.typography.bodySmall,
                    color = PriorityHelper.getDeadlineLabelColor(task.deadline),
                    modifier = Modifier.testTag("task_deadline_${task.id}")
                )
                Spacer(modifier = Modifier.height(4.dp))
                Badge(
                    containerColor = PriorityHelper.getColor(task.priority),
                    modifier = Modifier.testTag("task_priority_${task.id}")
                ) {
                    Text(
                        text = PriorityHelper.getLabel(task.priority),
                        color = MaterialTheme.colorScheme.onError,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            IconButton(
                onClick = { onEdit(task.id) },
                modifier = Modifier.testTag("btn_edit_task_${task.id}")
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Edit task")
            }

            IconButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.testTag("btn_delete_task_${task.id}")
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}