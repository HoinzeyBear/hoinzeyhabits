package com.example.hoinzeyshabits.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hoinzeyshabits.model.AchievedHabit
import com.example.hoinzeyshabits.model.Habit
import com.example.hoinzeyshabits.model.HabitFrequency
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime

@Database(
    entities = arrayOf(Habit::class, AchievedHabit::class),
    version = 1,
    exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun habitDao(): HabitDao
    abstract fun achievedHabitsDao(): AchievedHabitsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habits_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val scope: CoroutineScope)
        : RoomDatabase.Callback(){

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.habitDao(), database.achievedHabitsDao())
                }
            }
        }

        suspend fun populateDatabase(habitDao: HabitDao, achievedHabitsDao: AchievedHabitsDao) {
            habitDao.deleteAllHabits()

            val habit = Habit(name = "Good nights sleep",habitFrequency = HabitFrequency.DAILY)
            val habitId = habitDao.insert(habit)
            achievedHabitsDao.insert(AchievedHabit(habitId.toInt(), DateTime.now().withTimeAtStartOfDay(),false))
        }
    }
}