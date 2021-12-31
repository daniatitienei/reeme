package com.atitienei_daniel.reeme.data.reminders_db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM Reminder")
    fun getReminders(): Flow<List<Reminder>>

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReminder(reminder: Reminder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addReminder(reminder: Reminder)
}