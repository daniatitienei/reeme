package com.atitienei_daniel.reeme.data.reminders_db

import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    fun getReminders(): Flow<List<Reminder>>

    suspend fun deleteReminder(reminder: Reminder)

    suspend fun updateReminder(reminder: Reminder)

    suspend fun addReminder(reminder: Reminder)
}