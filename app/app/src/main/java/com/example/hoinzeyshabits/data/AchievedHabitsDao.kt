package com.example.hoinzeyshabits.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hoinzeyshabits.model.AchievedHabit

@Dao
interface AchievedHabitsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(habit: AchievedHabit)

    @Query("SELECT count(*) from achievedhabits where habitid=:id and achieveddate between :dateStart and :dateEnd")
    fun wasHabitAchievedThisDay(id:Int, dateStart: String, dateEnd: String): Int
}