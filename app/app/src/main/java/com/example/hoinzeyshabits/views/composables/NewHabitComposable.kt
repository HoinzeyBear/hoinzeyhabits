package com.example.hoinzeyshabits.views.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
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
fun NewHabitForm(
    modifier: Modifier = Modifier,
    state: NewHabitState,
    onHabitNameChanged: (name: String) -> Unit,
    onHabitFrequencyChanged: (habitFrequency: HabitFrequency) -> Unit,
    onHabitFrequencyCountChanged: (habitFrequencyCount: String) -> Unit,
    saveHabit: () -> Unit) {

    NewHabitTitle()
    Spacer(modifier = Modifier.height(32.dp))
    NewHabitName(modifier = Modifier.fillMaxWidth(), name = state.name, onNameChanged = onHabitNameChanged)
    NewHabitSpinner(modifier = Modifier.fillMaxWidth(), onHabitFrequencyChanged = onHabitFrequencyChanged)
    NewHabitFrequencyField(modifier = Modifier.fillMaxWidth(), onHabitFrequencyCountChanged = onHabitFrequencyCountChanged, count = state.habitFrequencyCount.toString())
    SaveNewHabitButton(onSaveNewHabit = saveHabit)
}

@Composable
fun NewHabitTitle() {
    Text(
        text = stringResource(R.string.habit_new),
        fontSize = 24.sp,
        fontWeight = FontWeight.Black)
}

@Composable
fun NewHabitName(
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
fun NewHabitSpinner(modifier: Modifier, onHabitFrequencyChanged: (habitFrequency: HabitFrequency) -> Unit) {
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
fun NewHabitFrequencyField(
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
fun SaveNewHabitButton(
    modifier: Modifier = Modifier,
    onSaveNewHabit: () -> Unit
) {
    Button(modifier = modifier,
        onClick = onSaveNewHabit) {
        Text(text = stringResource(R.string.save))
    }
}

//@Composable
//fun AuthenticationButton(
//    modifier: Modifier = Modifier,
//    authenticationMode: AuthenticationMode,
//    enableAuthentication: Boolean,
//    onAuthenticate: () -> Unit) {
//    Button(modifier = modifier,
//        onClick = onAuthenticate,
//        enabled = enableAuthentication) {
//
//        Text(text = stringResource(
//            if (authenticationMode ==
//                AuthenticationMode.SIGN_IN) {
//                R.string.action_sign_in
//            } else {
//                R.string.action_sign_up
//            }))
//    }
//}