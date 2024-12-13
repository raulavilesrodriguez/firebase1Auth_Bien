package com.example.firebase1.screens.task

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.firebase1.common.composable.ActionToolbar
import com.example.firebase1.common.ext.smallSpacer
import com.example.firebase1.common.ext.toolbarActions
import com.example.firebase1.model.Task
import com.example.firebase1.ui.theme.Firebase1Theme
import com.example.firebase1.R.string as AppText
import com.example.firebase1.R.drawable as AppIcon

@Composable
fun TasksScreen(
    openScreen: (Task) -> Unit,
    onAddClick: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
){
    val tasks = viewModel.tasks.collectAsStateWithLifecycle(emptyList())
    val options by viewModel.options

    TasksScreenContent(
        tasks = tasks.value,
        options = options,
        onAddClick = onAddClick,
        onTaskCheckChange = viewModel::onTaskCheckChange,
        onTaskActionClick = viewModel::onTaskActionClick,
        setSelectedTask = viewModel::setSelectedTask,
        openScreen = openScreen
    )

    LaunchedEffect(viewModel) { viewModel.loadTaskOptions() }
}

@Composable
fun TasksScreenContent(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    options: List<String>,
    onAddClick: () -> Unit,
    onTaskCheckChange: (Task) -> Unit,
    onTaskActionClick: ((Task) -> Unit, Task, String) -> Unit,
    setSelectedTask: (Task,(Task)-> Unit) -> Unit,
    openScreen: (Task) -> Unit
){
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAddClick()
                    setSelectedTask(Task(), openScreen)
                          },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = modifier.padding(16.dp)
            ) {
                Icon(Icons.Filled.Add, "Add")
            }
        }
    ){ innerPadding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {

            Spacer(modifier = Modifier.smallSpacer())
            LazyColumn(
                contentPadding = innerPadding
            ) {
                Log.d("OJOOO tasks", "TASKS: $tasks")
                items(tasks, key = { it.id }) { taskItem ->
                    TaskItem(
                        task = taskItem,
                        options = options,
                        onCheckChange = { onTaskCheckChange(taskItem) },
                        onActionClick = { action -> onTaskActionClick(openScreen, taskItem, action) }
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview(){
    val task = Task(
        title = "Task title",
        flag = true,
        completed = true
    )

    val options = TaskActionOption.getOptions(hasEditOption = true)

    Firebase1Theme {
        TasksScreenContent(
            tasks = listOf(task),
            options = options,
            onAddClick = { },
            onTaskCheckChange = { },
            onTaskActionClick = { _, _, _ -> },
            setSelectedTask = {_, _ ->},
            openScreen = { }
        )
    }
}