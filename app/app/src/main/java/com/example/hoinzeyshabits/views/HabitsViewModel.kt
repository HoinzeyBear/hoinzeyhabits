package com.example.hoinzeyshabits.views

import androidx.lifecycle.*
import com.example.hoinzeyshabits.data.HabitsRepository
import com.example.hoinzeyshabits.model.Habit
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HabitsViewModel(private val habitRepo: HabitsRepository)
    : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel
    val habits: LiveData<List<Habit>> = habitRepo.allHabits.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(habit: Habit) = viewModelScope.launch {
        habitRepo.insert(habit)
    }

    fun delete(habit: Habit) = viewModelScope.launch {
        habitRepo.delete(habit)
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