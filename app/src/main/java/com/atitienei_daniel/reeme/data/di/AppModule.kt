package com.atitienei_daniel.reeme.data.di

import android.app.Application
import androidx.compose.ui.graphics.Color
import com.atitienei_daniel.reeme.RemindersDatabase
import com.atitienei_daniel.reeme.data.ReminderDataSourceImpl
import com.atitienei_daniel.reeme.data.RemindersDataSource
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import com.atitienei_daniel.reeme.ui.utils.longToColor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import remindersdb.ReminderEntity
import java.util.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun provideSqlDriver(app: Application): SqlDriver = AndroidSqliteDriver(
        schema = RemindersDatabase.Schema,
        context = app,
        name = "reminders.db"
    )

    @Provides
    @Singleton
    fun provideReminderDataSource(driver: SqlDriver): RemindersDataSource =
        ReminderDataSourceImpl(
            db = RemindersDatabase(
                driver = driver, ReminderEntityAdapter = ReminderEntity.Adapter(
                    categoriesAdapter = listOfStringsAdapter,
                    repeatAdapter = repeatAdapter,
                    colorAdapter = colorAdapter,
                    dateAdapter = dateAdapter
                )
            )
        )
}

val listOfStringsAdapter = object : ColumnAdapter<List<String>, String> {
    override fun decode(databaseValue: String) =
        if (databaseValue.isEmpty()) {
            listOf()
        } else {
            databaseValue.split(",")
        }

    override fun encode(value: List<String>) = value.joinToString(separator = ",")
}

val colorAdapter = object : ColumnAdapter<Color, Long> {
    override fun decode(databaseValue: Long): Color = longToColor(databaseValue)

    override fun encode(value: Color): Long = value.value.toLong()
}

val repeatAdapter = object : ColumnAdapter<ReminderRepeatTypes, Long> {
    override fun decode(databaseValue: Long): ReminderRepeatTypes =
        when (databaseValue) {
            0L -> ReminderRepeatTypes.ONCE
            1L -> ReminderRepeatTypes.DAILY
            2L -> ReminderRepeatTypes.WEEKLY
            3L -> ReminderRepeatTypes.MONTHLY
            4L -> ReminderRepeatTypes.YEARLY
            else -> ReminderRepeatTypes.UNSELECTED
        }

    override fun encode(value: ReminderRepeatTypes): Long = value.ordinal.toLong()
}

val dateAdapter = object : ColumnAdapter<Calendar, Long> {
    override fun decode(databaseValue: Long): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time.time = databaseValue

        return calendar
    }

    override fun encode(value: Calendar): Long = value.time.time
}