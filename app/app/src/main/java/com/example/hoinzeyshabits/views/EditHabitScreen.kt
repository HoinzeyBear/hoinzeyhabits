package com.example.hoinzeyshabits.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.views.composables.EditHabitContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditHabitScreenRoot(habitId: Int) {
    val habitsRepo = (LocalContext.current.applicationContext as HabitsApplication).habitsRepository
    val habitsViewModel: HabitsViewModel = viewModel(factory = HabitsViewModelFactory(habitsRepo))
    val coroutineScope = rememberCoroutineScope()
    coroutineScope.launch {
        val habit = withContext(Dispatchers.IO) {
            habitsViewModel.getById(habitId);
        }
        habitsViewModel.initHabitState(habit)
    }
    MaterialTheme {
        EditHabitContent(
            modifier = Modifier.fillMaxWidth(),
            handleEvent = habitsViewModel::handleEvent,
            habitsViewModel.habitFormState.collectAsState().value
        )
    }
}