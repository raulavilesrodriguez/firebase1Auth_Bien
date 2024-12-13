package com.example.firebase1.screens.task

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.firebase1.model.Task
import com.example.firebase1.model.service.LogService
import com.example.firebase1.model.service.StorageService
import com.example.firebase1.model.service.ConfigurationService
import com.example.firebase1.screens.FireViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    logService: LogService,
    private val storageService: StorageService,
    private val configurationService: ConfigurationService
) : FireViewModel(logService){
    val options = mutableStateOf<List<String>>(listOf())

    val tasks = storageService.tasks
    val task1 = mutableStateOf(Task())

    init {
        Log.d("TASKSS", "TASKSS in ViewModel: $tasks")
    }

    fun loadTaskOptions() {
        val hasEditOption = configurationService.isShowTaskEditButtonConfig
        options.value = TaskActionOption.getOptions(hasEditOption)
    }

    fun onTaskCheckChange(task: Task) {
        launchCatching { storageService.update(task.copy(completed = !task.completed)) }
    }

    fun onTaskActionClick(openScreen: (Task) -> Unit, task: Task, action: String) {
        when (TaskActionOption.getByTitle(action)) {
            TaskActionOption.EditTask -> setSelectedTask(task, openScreen)
            TaskActionOption.ToggleFlag -> onFlagTaskClick(task)
            TaskActionOption.DeleteTask -> onDeleteTaskClick(task)
        }
    }

    fun setSelectedTask(task: Task, openScreen: (Task) -> Unit){
        task1.value = task
        openScreen(task)
    }

    private fun onFlagTaskClick(task: Task) {
        launchCatching { storageService.update(task.copy(flag = !task.flag)) }
    }

    private fun onDeleteTaskClick(task: Task) {
        launchCatching { storageService.delete(task.id) }
    }
}