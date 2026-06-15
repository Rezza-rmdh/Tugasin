package com.tugasin.app.model

data class Task(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val category: TaskCategory = TaskCategory.DAILY,
    val deadline: Long? = null,
    val priority: Priority = Priority.LOW,
    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class Priority {
    LOW, MEDIUM, HIGH
}

enum class TaskCategory(val label: String, val score: Int) {
    DAILY("Tugas / Latihan", 1),
    QUIZ("Kuis / Quiz", 2),
    REPORT("Makalah / Laporan", 3),
    MAJOR("UTS / UAS / Proyek Besar", 4)
}