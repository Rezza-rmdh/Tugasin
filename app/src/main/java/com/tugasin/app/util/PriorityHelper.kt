package com.tugasin.app.util

import androidx.compose.ui.graphics.Color
import com.tugasin.app.model.Priority
import com.tugasin.app.model.TaskCategory
import java.util.concurrent.TimeUnit

object PriorityHelper {

    fun getLabel(priority: Priority): String {
        return when (priority) {
            Priority.HIGH   -> "High"
            Priority.MEDIUM -> "Medium"
            Priority.LOW    -> "Low"
        }
    }

    fun getColor(priority: Priority): Color {
        return when (priority) {
            Priority.HIGH   -> Color(0xFFE53935)
            Priority.MEDIUM -> Color(0xFFFB8C00)
            Priority.LOW    -> Color(0xFF43A047)
        }
    }

    fun fromLabel(label: String): Priority {
        return when (label.uppercase()) {
            "HIGH" -> Priority.HIGH
            "LOW"  -> Priority.LOW
            else   -> Priority.MEDIUM
        }
    }

    // Kalkulasi priority otomatis hanya dari kategori
    // Skor 1 → LOW, Skor 2-3 → MEDIUM, Skor 4 → HIGH
    fun calculatePriority(category: TaskCategory): Priority {
        return when (category.score) {
            1    -> Priority.LOW
            4    -> Priority.HIGH
            else -> Priority.MEDIUM
        }
    }

    fun getCategoryScore(category: TaskCategory): Int {
        return category.score
    }

    // Label waktu tersisa (tetap ada untuk ditampilkan di UI)
    fun getDeadlineLabel(deadlineMillis: Long?): String {
        if (deadlineMillis == null) return "Tidak ada deadline"
        val now = System.currentTimeMillis()
        val daysLeft = TimeUnit.MILLISECONDS.toDays(deadlineMillis - now)
        return when {
            deadlineMillis < now -> "Deadline terlewat!"
            daysLeft == 0L       -> "Kurang dari 24 jam"
            daysLeft == 1L       -> "1 hari lagi"
            else                 -> "$daysLeft hari lagi"
        }
    }

    fun getDeadlineLabelColor(deadlineMillis: Long?): Color {
        if (deadlineMillis == null) return Color(0xFF9E9E9E)
        val now = System.currentTimeMillis()
        val daysLeft = TimeUnit.MILLISECONDS.toDays(deadlineMillis - now)
        return when {
            deadlineMillis < now -> Color(0xFFE53935)
            daysLeft == 0L       -> Color(0xFFE53935)
            daysLeft <= 3        -> Color(0xFFFB8C00)
            else                 -> Color(0xFF43A047)
        }
    }
}