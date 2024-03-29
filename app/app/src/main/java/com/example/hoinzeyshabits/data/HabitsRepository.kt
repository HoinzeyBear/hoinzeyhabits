package com.example.hoinzeyshabits.data

import androidx.annotation.WorkerThread
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.pojo.HabitsWithAchievedDates
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class HabitsRepository(
    private val habitDao: HabitDao,
    private val achievedHabitsDao: AchievedHabitsDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
//    val allHabits: Flow<List<Habit>> = habitDao.getAllHabitsInOrderAsFlow()
    val allHabitsWithDates: Flow<List<HabitsWithAchievedDates>> = habitDao.getAllHabitsWithDatesInOrderAsFlow()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long {
        return habitDao.insert(habit)
    }

    @WorkerThread
    @Delete
    suspend fun delete(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    @WorkerThread
    @Delete
    suspend fun delete(id: Int) {
        habitDao.deleteHabit(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getByID(id: Int): Habit {
        return habitDao.getById(id)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAchieveHabit(achievedHabit: AchievedHabit) {
        achievedHabitsDao.insert(achievedHabit)
    }
}