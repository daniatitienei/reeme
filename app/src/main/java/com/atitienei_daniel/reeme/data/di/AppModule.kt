package com.atitienei_daniel.reeme.data.di

import android.app.Application
import androidx.room.Room
import com.atitienei_daniel.reeme.data.reminders_db.ReminderDatabase
import com.atitienei_daniel.reeme.data.reminders_db.ReminderRepository
import com.atitienei_daniel.reeme.data.reminders_db.ReminderRepositoryImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun provideRemindersDatabase(app: Application): ReminderDatabase = Room.databaseBuilder(
        app,
        ReminderDatabase::class.java,
        "reminders_db"
    ).build()


    @Provides
    @Singleton
    fun provideRemindersRepository(db: ReminderDatabase): ReminderRepository = ReminderRepositoryImpl(dao = db.dao)
}