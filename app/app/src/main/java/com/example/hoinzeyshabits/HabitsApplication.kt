package com.example.hoinzeyshabits

import android.app.Application
import com.example.hoinzeyshabits.data.AppDatabase
import com.example.hoinzeyshabits.data.HabitsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class HabitsApplication: Application() {

    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { HabitsRepository(database.habitDao()) }
}