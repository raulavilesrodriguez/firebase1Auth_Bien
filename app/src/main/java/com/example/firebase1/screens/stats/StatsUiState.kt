package com.example.firebase1.screens.stats

data class StatsUiState(
    val completedTasksCount: Int = 0,
    val importantCompletedTasksCount: Int = 0,
    val mediumHighTasksToCompleteCount: Int = 0
)
