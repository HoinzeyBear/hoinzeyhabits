package com.example.hoinzeyshabits.views

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.views.composables.EditHabitContent

@Composable
fun NewHabitScreenRoot() {
    val habitsRepo = (LocalContext.current.applicationContext as HabitsApplication).habitsRepository
    val habitsViewModel: HabitsViewModel = viewModel(factory = HabitsViewModelFactory(habitsRepo))
    MaterialTheme {
        EditHabitContent(
            modifier = Modifier.fillMaxWidth(),
            handleEvent = habitsViewModel::handleEvent,
            habitsViewModel.habitFormState.collectAsState().value)
    }
}