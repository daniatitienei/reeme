package com.atitienei_daniel.reeme.data.reminders_db

import kotlinx.coroutines.flow.Flow

class ReminderRepositoryImpl(
    private val dao: ReminderDao
): ReminderRepository {

    override fun getReminders(): Flow<List<Reminder>> =
        dao.getReminders()


    override suspend fun deleteReminder(reminder: Reminder) {
        dao.deleteReminder(reminder = reminder)
    }

    override suspend fun updateReminder(reminder: Reminder) {
        dao.updateReminder(reminder = reminder)
    }

    override suspend fun addReminder(reminder: Reminder) {
        dao.addReminder(reminder = reminder)
    }
}