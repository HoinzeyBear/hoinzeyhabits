package com.example.hoinzeyshabits.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hoinzeyshabits.HabitsApplication
import com.example.hoinzeyshabits.views.composables.HabitListState

/*
    Day navigation
    List of habits

    Habit:
        Name
        Button to complete it
        Long press
 */

@Composable
fun HabitsListRoot() {
    val habitsRepo = (LocalContext.current.applicationContext as HabitsApplication).habitsRepository
    val habitsViewModel: HabitsViewModel = viewModel(factory = HabitsViewModelFactory(habitsRepo))

    HabitsListContent(listState = habitsViewModel.habitListState.value)
//    LaunchedEffect(Unit) {
//        habitsViewModel.loadListContent()
//    }
}

@Composable
fun HabitsListContent(listState: HabitListState) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(items = listState.habitList) {
            HabitItem(
                habitName = it.habit?.name ?: "",
                habitAchieved = it.achievedOnDate(listState.targetDate),
                {}
            )
        }
    }
}

@Composable
fun HabitItem(
    habitName: String,
    habitAchieved: Boolean,
    onHabitAchieved: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = habitName)
        Checkbox(checked = habitAchieved, onCheckedChange = {})
    }
}