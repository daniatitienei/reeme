package com.atitienei_daniel.reeme.ui.screens.create_reminder

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.RemindersDataSource
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val repository: RemindersDataSource
) : ViewModel() {

    private var _titleError = mutableStateOf<String?>(null)
    val titleError: State<String?> = _titleError

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: CreateReminderEvents) {
        when (event) {
            is CreateReminderEvents.OnCancelClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }
            is CreateReminderEvents.OnCreateCategoryAlertClick -> {
                sendUiEvent(UiEvent.AlertDialog().copy(isOpen = true))
            }
            is CreateReminderEvents.OnCreateCategoryAlertDismiss -> {
                sendUiEvent(UiEvent.AlertDialog().copy(isOpen = false))
            }
            is CreateReminderEvents.OnCreateReminderClick -> {
                viewModelScope.launch {
                    repository.insertReminder(reminder = event.reminder)
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
    }

    fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
