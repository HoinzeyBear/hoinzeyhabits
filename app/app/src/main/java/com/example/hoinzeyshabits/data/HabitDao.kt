package com.example.hoinzeyshabits.data

import androidx.room.*
import com.example.hoinzeyshabits.model.Habit
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit)

    @Query("SELECT * from habits order by habitid asc")
    suspend fun getAllHabitsInOrder(): List<Habit>

    @Query("SELECT * from habits order by habitid asc")
    fun getAllHabitsInOrderAsFlow(): Flow<List<Habit>>

    @Query("SELECT * from habits where habitid=:id")
    fun getById(id: Int): Habit

    @Query("DELETE from habits where habitid=:id")
    suspend fun deleteHabit(id: Int)

    @Delete
    suspend fun deleteHabit(habit: Habit)

    @Query("DELETE from habits")
    suspend fun deleteAllHabits()
}