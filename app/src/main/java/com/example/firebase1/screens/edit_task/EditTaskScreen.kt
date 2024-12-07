package com.example.firebase1.screens.edit_task

import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase1.common.composable.ActionToolbar
import com.example.firebase1.common.composable.BasicField
import com.example.firebase1.common.composable.CardSelector
import com.example.firebase1.common.composable.RegularCardEditor
import com.example.firebase1.common.ext.card
import com.example.firebase1.common.ext.fieldModifier
import com.example.firebase1.common.ext.spacer
import com.example.firebase1.common.ext.toolbarActions
import com.example.firebase1.model.Priority
import com.example.firebase1.model.Task
import com.example.firebase1.ui.theme.Firebase1Theme
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.example.firebase1.R.drawable as AppIcon
import com.example.firebase1.R.string as AppText

@Composable
fun EditTaskScreen(
    popUpScreen: () -> Unit,
    viewModel: EditTaskViewModel = hiltViewModel()
){
    val task by viewModel.task
    val activity = LocalContext.current as AppCompatActivity

    EditTaskScreenContent(
        task = task,
        onDoneClick = { viewModel.onDoneClick(popUpScreen) },
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onUrlChange = viewModel::onUrlChange,
        onDateChange = viewModel::onDateChange,
        onTimeChange = viewModel::onTimeChange,
        onPriorityChange = viewModel::onPriorityChange,
        onFlagToggle = viewModel::onFlagToggle,
        activity = activity
    )
}

@Composable
fun EditTaskScreenContent(
    modifier: Modifier = Modifier,
    task: Task,
    onDoneClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onUrlChange: (String) -> Unit,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    onPriorityChange: (String) -> Unit,
    onFlagToggle: (String) -> Unit,
    activity: AppCompatActivity?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionToolbar(
            title = AppText.edit_task,
            modifier = Modifier.toolbarActions(),
            primaryActionIcon = AppIcon.ic_check,
            primaryAction = { onDoneClick() }
        )

        Spacer(modifier = Modifier.spacer())

        val fieldModifier = Modifier.fieldModifier()
        BasicField(AppText.title, task.title, onTitleChange, fieldModifier)
        BasicField(AppText.description, task.description, onDescriptionChange, fieldModifier)
        BasicField(AppText.url, task.url, onUrlChange, fieldModifier)

        Spacer(modifier = Modifier.spacer())
        CardEditors(task, onDateChange, onTimeChange, activity)
        CardSelectors(task, onPriorityChange, onFlagToggle)

        Spacer(modifier = Modifier.spacer())
    }
}

@Composable
private fun CardEditors(
    task: Task,
    onDateChange: (Long) -> Unit,
    onTimeChange: (Int, Int) -> Unit,
    activity: AppCompatActivity?
) {
    RegularCardEditor(AppText.date, AppIcon.ic_calendar, task.dueDate, Modifier.card()) {
        showDatePicker(activity, onDateChange)
    }

    RegularCardEditor(AppText.time, AppIcon.ic_clock, task.dueTime, Modifier.card()) {
        showTimePicker(activity, onTimeChange)
    }
}

@Composable
private fun CardSelectors(
    task: Task,
    onPriorityChange: (String) -> Unit,
    onFlagToggle: (String) -> Unit
) {
    val prioritySelection = Priority.getByName(task.priority).name
    CardSelector(AppText.priority, Priority.getOptions(), prioritySelection, Modifier.card()) {
            newValue ->
        onPriorityChange(newValue)
    }

    val flagSelection = EditFlagOption.getByCheckedState(task.flag).name
    CardSelector(AppText.flag, EditFlagOption.getOptions(), flagSelection, Modifier.card()) { newValue
        ->
        onFlagToggle(newValue)
    }
}

private fun showDatePicker(activity: AppCompatActivity?, onDateChange: (Long) -> Unit) {
    val picker = MaterialDatePicker.Builder.datePicker().build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { timeInMillis -> onDateChange(timeInMillis) }
    }
}

private fun showTimePicker(activity: AppCompatActivity?, onTimeChange: (Int, Int) -> Unit) {
    val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()

    activity?.let {
        picker.show(it.supportFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener { onTimeChange(picker.hour, picker.minute) }
    }
}

@Preview(showBackground = true)
@Composable
fun EditTaskScreenPreview() {
    val task = Task(
        title = "Task title",
        description = "Task description",
        flag = true
    )

    Firebase1Theme {
        EditTaskScreenContent(
            task = task,
            onDoneClick = { },
            onTitleChange = { },
            onDescriptionChange = { },
            onUrlChange = { },
            onDateChange = { },
            onTimeChange = { _, _ -> },
            onPriorityChange = { },
            onFlagToggle = { },
            activity = null
        )
    }
}

