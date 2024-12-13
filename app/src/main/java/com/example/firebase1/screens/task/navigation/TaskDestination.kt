package com.example.firebase1.screens.task.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.firebase1.R.string as AppText
import com.example.firebase1.R.drawable as AppIcon

/** Navigation destination when an user login */
enum class TaskDestination(
    @StringRes val labelRes: Int,
    @DrawableRes val icon: Int
) {
    Stats(AppText.stats, AppIcon.ic_stats),
    Settings(AppText.settings, AppIcon.ic_settings),
    Tasks(AppText.tasks, AppIcon.ic_task),
    Add(AppText.add, AppIcon.ic_add)
}