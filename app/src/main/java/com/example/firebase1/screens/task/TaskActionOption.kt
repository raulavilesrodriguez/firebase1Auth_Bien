package com.example.firebase1.screens.task

enum class TaskActionOption(val title: String) {
    EditTask("Edit task"),
    ToggleFlag("Toggle flag"),
    DeleteTask("Delete task");

    companion object {
        fun getByTitle(title: String): TaskActionOption {
            entries.forEach { action -> if (title == action.title) return action }

            return EditTask
        }

        fun getOptions(hasEditOption: Boolean): List<String> {
            val options = mutableListOf<String>()
            entries.forEach { taskAction ->
                if (hasEditOption || taskAction != EditTask) {
                    options.add(taskAction.title)
                }
            }
            return options
        }
    }
}