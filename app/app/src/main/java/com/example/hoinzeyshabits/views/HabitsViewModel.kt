package com.example.hoinzeyshabits.views

import android.util.Log
import androidx.lifecycle.*
import com.example.hoinzeyshabits.data.HabitsRepository
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.pojo.HabitsWithAchievedDates
import kotlinx.coroutines.launch
import org.joda.time.DateTime

class HabitsViewModel(private val habitRepo: HabitsRepository)
    : ViewModel() {

    // Using LiveData and caching what allHabits returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    val habits: LiveData<List<HabitsWithAchievedDates>> = habitRepo.allHabitsWithDates.asLiveData()
    var targetDate: DateTime = DateTime.now().withTimeAtStartOfDay()

    fun printHabitsForDisplay() = viewModelScope.launch {
        Log.d("HVM", "$habits")
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(habit: Habit) = viewModelScope.launch {
        Log.d("HVM", "I'm on thread ${Thread.currentThread()}")
        val habitId = habitRepo.insert(habit)
//        for(i in -10..10) {
//            habitRepo.insertAchieveHabit(AchievedHabit(habitId.toInt(), DateTime.now().plusDays(i).withTimeAtStartOfDay(), false))
//        }
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
}

class HabitsViewModelFactory(private val repository: HabitsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HabitsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}