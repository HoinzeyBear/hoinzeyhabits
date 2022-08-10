package com.example.hoinzeyshabits.views.composables

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.model.HabitFrequency
import com.example.hoinzeyshabits.views.HabitsViewModel
import com.example.hoinzeyshabits.views.HabitsViewModelFactory

@Composable
fun NewHabit() {
    val habitsRepo = (LocalContext.current.applicationContext as HabitsApplication).habitsRepository
    val habitsViewModel: HabitsViewModel = viewModel(factory = HabitsViewModelFactory(habitsRepo))
    MaterialTheme {
        NewHabitBody(
            modifier = Modifier.fillMaxWidth(),
            handleEvent = habitsViewModel::handleEvent,
            habitsViewModel.newHabitState.collectAsState().value)
    }
}

@Composable
fun NewHabitBody(
    modifier: Modifier = Modifier,
    handleEvent: (event: HabitsViewModel.HabitEvent) -> Unit,
    state: NewHabitState) {
    Column {
        NewHabitForm(
            modifier = Modifier.fillMaxWidth(),
            state = state,
            onHabitNameChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitNameChanged(it)) },
            onHabitFrequencyChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitFrequencyChanged(it)) },
            onHabitFrequencyCountChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitFrequencyCountChanged(it)) },
            saveHabit = {  }
        )
    }
}

@Composable
fun NewHabitForm(
    modifier: Modifier = Modifier,
    state: NewHabitState,
    onHabitNameChanged: (name: String) -> Unit,
    onHabitFrequencyChanged: (habitFrequency: HabitFrequency) -> Unit,
    onHabitFrequencyCountChanged: (habitFrequencyCount: String) -> Unit,
    saveHabit: () -> Unit) {

    NewHabitTitle()
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun NewHabitTitle() {
    Text(
        text = stringResource(R.string.habit_new),
        fontSize = 24.sp,
        fontWeight = FontWeight.Black)
}