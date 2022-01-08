package com.example.hoinzeyshabits.data

import androidx.room.*
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.views.HabitForDisplay
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: Habit): Long

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

    @Query("SELECT habitid,name,habitFrequency,habitFrequencyCount,creationDate,achieved FROM habits inner join achievedhabits using (habitid) where achieveddate between :dateStart and :dateEnd order by habitid asc")
    suspend fun getHabitsToDisplayForDate(dateStart: String, dateEnd: String): List<HabitForDisplay>
}