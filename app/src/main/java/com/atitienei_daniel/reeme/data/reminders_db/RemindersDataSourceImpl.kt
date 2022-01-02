package com.atitienei_daniel.reeme.data.reminders_db

import com.atitienei_daniel.reeme.RemindersDatabase
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import remindersdb.ReminderEntity

class RemindersDataSourceImpl(
    db: RemindersDatabase
) : RemindersDataSource {

    private val queries = db.reminderEntityQueries

    override suspend fun getReminderById(id: Long): ReminderEntity? {
        return withContext(Dispatchers.IO) {
            queries.getReminderById(id = id).executeAsOneOrNull()
        }
    }

    override fun getReminders(): Flow<List<ReminderEntity>> {
        return queries.getReminders().asFlow().mapToList()
    }

    override suspend fun insertReminder(reminder: Reminder) {
        withContext(Dispatchers.IO) {
            queries.insertReminder(
                id = null,
                title = reminder.title,
                description = reminder.description,
                repeat = reminder.repeat,
                color = reminder.color,
                isDone = reminder.isDone,
                isPinned = reminder.isPinned,
                categories = reminder.categories,
                date = reminder.date.time
            )
        }
    }

    override suspend fun deleteReminderById(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteReminderById(id = id)
        }
    }

    override suspend fun updateReminder(reminder: ReminderEntity) {
        withContext(Dispatchers.IO) {
            queries.updateReminder(
                id = reminder.id,
                isDone = reminder.isDone,
                isPinned = reminder.isPinned,
                title = reminder.title,
                description = reminder.description,
                repeat = reminder.repeat,
                color = reminder.color,
                categories = reminder.categories,
                date = reminder.date
            )
        }
    }
}