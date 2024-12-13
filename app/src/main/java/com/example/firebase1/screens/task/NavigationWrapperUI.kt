package com.example.firebase1.screens.task

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.firebase1.screens.edit_task.EditTaskScreen
import com.example.firebase1.screens.settings.SettingsScreen
import com.example.firebase1.screens.stats.StatsScreen
import com.example.firebase1.screens.task.navigation.TaskDestination


private val WINDOW_WIDTH_LARGE = 1200.dp

@Composable
fun NavigationWrapperUI(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
){
    var selectedDestination : TaskDestination by remember {
        mutableStateOf(TaskDestination.Tasks)
    }

    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }

    val navLayoutType = if (windowSize.width >= WINDOW_WIDTH_LARGE) {
        // Show a permanent drawer when window width is large.
        NavigationSuiteType.NavigationDrawer
    } else {
        // Otherwise use the default from NavigationSuiteScaffold.
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(currentWindowAdaptiveInfo())
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            TaskDestination.entries.forEach {
                item(
                    label = { Text(text = stringResource(id = it.labelRes)) },
                    icon = { Icon(painter = painterResource(it.icon), contentDescription = null) },
                    selected = it == selectedDestination,
                    onClick = {selectedDestination = it}
                )
            }
        },
        layoutType = navLayoutType
    ){
        when(selectedDestination){
            TaskDestination.Tasks -> TasksDestination(
                onAddClick = {selectedDestination = TaskDestination.Add}
            )
            TaskDestination.Settings -> SettingsDestination(restartApp, openScreen)
            TaskDestination.Stats -> StatsDestination()
            TaskDestination.Add -> AddDestination(
                onNavigateToTasks = {selectedDestination = TaskDestination.Tasks}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TasksDestination(
    onAddClick: () -> Unit
){
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                TasksScreen(
                    openScreen = { task ->
                        var id = 0
                        if(task.id != ""){ id = task.id.toInt()}
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, id)
                    },
                    onAddClick = onAddClick,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                EditTaskScreen(
                    popUpScreen = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.List)
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsDestination(
    restartApp: (String) -> Unit,
    openScreen: (String) -> Unit,
){
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                SettingsScreen(restartApp, openScreen)
            }
        },
        detailPane = {}
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun StatsDestination(){
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                StatsScreen()
            }
        },
        detailPane = {}
    )
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun AddDestination(
    onNavigateToTasks: () -> Unit
){
    val navigator = rememberListDetailPaneScaffoldNavigator<Long>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                EditTaskScreen(
                    popUpScreen = {
                        onNavigateToTasks()
                    }
                )
            }
        },
        detailPane = {}
    )
}

