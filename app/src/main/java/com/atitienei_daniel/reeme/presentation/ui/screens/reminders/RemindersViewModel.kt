package com.atitienei_daniel.reeme.presentation.ui.screens.reminders

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atitienei_daniel.reeme.domain.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RemindersViewModel @Inject constructor(

) : ViewModel() {

    private val _state = mutableStateOf<List<Reminder>>(listOf())
    val state: State<List<Reminder>> = _state

    init {
        getReminders()
    }

    private fun getReminders() {

    }
}