package com.example.firebase1.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.firebase1.common.composable.BasicToolbar
import com.example.firebase1.common.ext.smallSpacer
import com.example.firebase1.ui.theme.Firebase1Theme
import com.example.firebase1.R.string as AppText

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState

    StatsScreenContent(uiState = uiState)
}

@Composable
fun StatsScreenContent(
    modifier: Modifier = Modifier,
    uiState: StatsUiState
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(AppText.stats)

        Spacer(modifier = Modifier.smallSpacer())

        StatsItem(titleRes = AppText.completed_tasks, value = uiState.completedTasksCount)
        StatsItem(titleRes = AppText.important_completed_tasks, value = uiState.importantCompletedTasksCount)
        StatsItem(titleRes = AppText.medium_high_tasks_to_complete, value = uiState.mediumHighTasksToCompleteCount)
    }
}

@Composable
fun StatsItem(titleRes: Int, value: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp, vertical = 24.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )

                Text(text = "$value", fontSize = 48.sp)
            }
        }
    }

    Spacer(modifier = Modifier.smallSpacer())
}

@Preview(showBackground = true)
@Composable
fun StatsScreenPreview() {
    Firebase1Theme {
        StatsScreenContent(
            uiState = StatsUiState()
        )
    }
}