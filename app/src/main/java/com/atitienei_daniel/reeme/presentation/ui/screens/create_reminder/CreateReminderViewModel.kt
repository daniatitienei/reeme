package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.reminders_db.Reminder
import com.atitienei_daniel.reeme.data.reminders_db.ReminderRepository
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val repository: ReminderRepository
) : ViewModel() {

    private var _titleError = mutableStateOf<String?>(null)
    val titleError: State<String?> = _titleError

    fun addReminder(reminder: Reminder) {
        viewModelScope.launch {
            repository.addReminder(reminder = reminder)
        }
    }
}
