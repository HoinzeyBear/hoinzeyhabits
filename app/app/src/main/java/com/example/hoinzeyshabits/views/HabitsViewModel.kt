package com.example.hoinzeyshabits.views

import android.util.Log
import androidx.lifecycle.*
import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.example.hoinzeyshabits.data.DateTimeConverter
import com.example.hoinzeyshabits.data.HabitsRepository
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class HabitsViewModel(private val habitRepo: HabitsRepository)
    : ViewModel() {

    // Using LiveData and caching what allHabits returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    val habits: LiveData<List<Habit>> = habitRepo.allHabits.asLiveData()
    var habitsForDisplay: List<HabitForDisplay>? = listOf()
    var targetDate: DateTime = DateTime.now()

    fun printHabitsForDisplay() = viewModelScope.launch {
        Log.d("HVM", "${habitRepo.getHabitsForDisplay(targetDate)}")
    }

    suspend fun getHabitsToDisplay(): List<HabitForDisplay> = coroutineScope {
        habitsForDisplay = habitRepo.getHabitsForDisplay(targetDate)
        return@coroutineScope habitsForDisplay!!
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(habit: Habit) = viewModelScope.launch {
        Log.d("HVM", "I'm on thread ${Thread.currentThread()}")
        val habitId = habitRepo.insert(habit)
        for(i in -10..10) {
            habitRepo.insertAchieveHabit(AchievedHabit(habitId.toInt(), DateTime.now().plusDays(i).withTimeAtStartOfDay(), false))
        }
//        habitRepo.insertAchieveHabit(AchievedHabit(habitId.toInt(), DateTime.now().withTimeAtStartOfDay(), false))
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        Log.d("HVM", "I'm on thread ${Thread.currentThread()}")
        habitRepo.delete(habit)
    }

    fun delete(id: Int) = viewModelScope.launch {
        Log.d("HVM", "I'm on thread ${Thread.currentThread()}")
        habitRepo.delete(id)
    }

    fun insert(achievedHabit: AchievedHabit) = viewModelScope.launch {
        Log.d("HVM", "I'm on thread ${Thread.currentThread()}")
        habitRepo.insertAchieveHabit(achievedHabit)
    }

    suspend fun getById(id: Int): Habit {
        return habitRepo.getByID(id)
    }

//    suspend fun getAchievedHabitsForHabitList(): HashSet<Int> =
//        withContext(viewModelScope.coroutineContext) {
//            makeSet()
//        }

    fun makeSet(): HashSet<Int> {
        val achievedHabits = hashSetOf<Int>()
        for(habit in habits.value!!) {
            if(habitRepo.wasAchievedThisDay(habit.habitId, DateTime())) {
                achievedHabits.add(habit.habitId)
            }
        }
        return achievedHabits
    }
}

@TypeConverters(DateTimeConverter::class)
data class HabitForDisplay(
    @ColumnInfo(name = "habitid")
    var habitId: Int = 0,
    val name: String,
    val habitFrequency: HabitFrequency,
    val habitFrequencyCount: Int = 1,
    val creationDate: DateTime = DateTime(),
    var achieved: Boolean = false
)

class HabitsViewModelFactory(private val repository: HabitsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}