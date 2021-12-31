package com.atitienei_daniel.reeme.data.reminders_db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Reminder::class],
    version = 1
)
abstract class ReminderDatabase: RoomDatabase() {

    abstract val dao: ReminderDao
}