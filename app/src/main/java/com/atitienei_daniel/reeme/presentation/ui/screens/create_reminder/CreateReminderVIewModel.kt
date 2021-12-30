package com.atitienei_daniel.reeme.presentation.ui.screens.create_reminder

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atitienei_daniel.reeme.domain.model.Reminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateReminderViewModel @Inject constructor(

) : ViewModel() {

    private var _titleError = mutableStateOf<String?>(null)
    val titleError: State<String?> = _titleError

    fun createReminder(reminder: Reminder) {
        if (reminder.title.isEmpty()) {
            _titleError.value = "Title must be completed."
            return
        }
        /*TODO*/
    }
}
