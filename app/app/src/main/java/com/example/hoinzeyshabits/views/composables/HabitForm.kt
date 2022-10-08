package com.example.hoinzeyshabits.views.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hoinzeyshabits.R
import com.example.hoinzeyshabits.model.HabitFrequency
import com.example.hoinzeyshabits.views.HabitsViewModel

@Composable
fun EditHabitContent(
    modifier: Modifier = Modifier,
    handleEvent: (event: HabitsViewModel.HabitEvent) -> Unit,
    state: HabitFormState) {
    Column {
        EditHabitForm(
            modifier = modifier.fillMaxWidth(),
            state = state,
            onHabitNameChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitNameChanged(it)) },
            onHabitFrequencyChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitFrequencyChanged(it)) },
            onHabitFrequencyCountChanged = { handleEvent(HabitsViewModel.HabitEvent.NewHabitFrequencyCountChanged(it)) },
            saveHabit = { handleEvent(HabitsViewModel.HabitEvent.SaveHabit) }
        )
    }
}

@Composable
fun EditHabitForm(
    modifier: Modifier = Modifier,
    state: HabitFormState,
    onHabitNameChanged: (name: String) -> Unit,
    onHabitFrequencyChanged: (habitFrequency: HabitFrequency) -> Unit,
    onHabitFrequencyCountChanged: (habitFrequencyCount: String) -> Unit,
    saveHabit: () -> Unit) {

    EditHabitTitle(state.formMode)
    Spacer(modifier = Modifier.height(32.dp))
    EditHabitName(modifier = Modifier.fillMaxWidth(), name = state.name, onNameChanged = onHabitNameChanged)
    EditHabitSpinner(modifier = Modifier.fillMaxWidth(), onHabitFrequencyChanged = onHabitFrequencyChanged)
    EditHabitFrequencyField(modifier = Modifier.fillMaxWidth(), onHabitFrequencyCountChanged = onHabitFrequencyCountChanged, count = state.habitFrequencyCount.toString())
    SaveHabitButton(onSaveNewHabit = saveHabit)
}

@Composable
fun EditHabitTitle(formMode: HabitFormMode) {
    val text = if(formMode == HabitFormMode.EDIT_HABIT) {
        stringResource(R.string.habit_new)
    } else stringResource(R.string.habit_edit)
    Text(
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black)
}

@Composable
fun EditHabitName(
    modifier: Modifier,
    name: String?,
    onNameChanged: (habitname: String) -> Unit) {

    TextField(
        modifier = modifier,
        value = name ?: "",
        onValueChange = { name -> onNameChanged(name)},
        label = {
            Text(text = stringResource(R.string.habit))
        })
}

@Composable
fun EditHabitSpinner(modifier: Modifier, onHabitFrequencyChanged: (habitFrequency: HabitFrequency) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val items = stringArrayResource(id = R.array.habit_frequency_options)
    Box(modifier = modifier.wrapContentSize(Alignment.TopStart)) {
        Text(
            text = HabitFrequency.values()[selectedIndex].name,
            modifier = modifier.clickable { expanded = true })
        DropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest =  { expanded = false },
//        offset = DpOffset(16.dp, 0.dp)
        ) {

            items.forEachIndexed{ index, s ->
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedIndex = index
                    onHabitFrequencyChanged(HabitFrequency.values()[index])
                }) {
                    Text(text = s)
                }
            }
        }
    }
}

@Composable
fun EditHabitFrequencyField(
    modifier: Modifier,
    count: String?,
    onHabitFrequencyCountChanged: (habitFrequencyCount: String) -> Unit
) {
    TextField(
        modifier = modifier,
        value = count ?: "",
        onValueChange = { newCount -> onHabitFrequencyCountChanged(newCount)},
        label = {
            Text(text = stringResource(R.string.times_per_day))
        })
}

@Composable
fun SaveHabitButton(
    modifier: Modifier = Modifier,
    onSaveNewHabit: () -> Unit
) {
    Button(modifier = modifier,
        onClick = onSaveNewHabit) {
        Text(text = stringResource(R.string.save))
    }
}