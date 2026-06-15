package com.tugasin.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.tugasin.app.model.Priority
import com.tugasin.app.util.PriorityHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortSheet(
    selectedPriority: Priority?,
    selectedCompleted: Boolean?,
    sortByPriority: Boolean,
    onPrioritySelected: (Priority?) -> Unit,
    onCompletedSelected: (Boolean?) -> Unit,
    onSortByPriorityChanged: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_sort_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Filter & Sort",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(text = "Priority", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedPriority == null,
                    onClick = { onPrioritySelected(null) },
                    label = { Text("All") },
                    modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_priority_all")
                )
                Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = selectedPriority == priority,
                        onClick = { onPrioritySelected(priority) },
                        label = { Text(PriorityHelper.getLabel(priority)) },
                        modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_priority_${priority.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Status", style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilterChip(
                    selected = selectedCompleted == null,
                    onClick = { onCompletedSelected(null) },
                    label = { Text("All") },
                    modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_status_all")
                )
                FilterChip(
                    selected = selectedCompleted == false,
                    onClick = { onCompletedSelected(false) },
                    label = { Text("Active") },
                    modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_status_active")
                )
                FilterChip(
                    selected = selectedCompleted == true,
                    onClick = { onCompletedSelected(true) },
                    label = { Text("Completed") },
                    modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("filter_status_completed")
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sort by priority",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = sortByPriority,
                    onCheckedChange = onSortByPriorityChanged,
                    modifier = Modifier.semantics { testTagsAsResourceId = true }.testTag("switch_sort_by_priority")
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTagsAsResourceId = true }
                    .testTag("btn_apply_filter")
            ) {
                Text("Apply")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}