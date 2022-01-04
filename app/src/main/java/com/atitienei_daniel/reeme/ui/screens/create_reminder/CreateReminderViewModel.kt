package com.atitienei_daniel.reeme.ui.screens.create_reminder

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.datastore.StoreCategories
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.dateToString
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReminderViewModel @Inject constructor(
    private val repository: RemindersDataSource,
    private val storeCategories: StoreCategories,
) : ViewModel() {

    private var _titleError = mutableStateOf<String?>(null)
    val titleError: State<String?> = _titleError

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun getCategories(): Flow<MutableList<String>?> = storeCategories.getCategories

    private fun insertCategory(categories: MutableList<String>) {
        viewModelScope.launch {
            storeCategories.insertCategory(categories = categories)
        }
    }

    fun onEvent(event: CreateReminderEvents) {
        when (event) {
            is CreateReminderEvents.OnCancelClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }
            is CreateReminderEvents.OpenCreateCategoryAlert -> {
                sendUiEvent(UiEvent.AlertDialog().copy(isOpen = true))
            }
            is CreateReminderEvents.DismissCreateCategoryAlert -> {
                sendUiEvent(UiEvent.AlertDialog().copy(isOpen = false))
            }
            is CreateReminderEvents.OnCreateReminderClick -> {
                viewModelScope.launch {
                    repository.insertReminder(reminder = event.reminder)
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
            is CreateReminderEvents.OpenDatePicker -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = true))
            }
            is CreateReminderEvents.DismissDatePicker -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = false))
            }
            is CreateReminderEvents.OpenTimePicker -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = true))
            }
            is CreateReminderEvents.DismissTimePicker -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = false))
            }
            is CreateReminderEvents.InsertCategory -> {
                insertCategory(categories = event.categories)
                sendUiEvent(UiEvent.AlertDialog(isOpen = false))
            }
            is CreateReminderEvents.ToggleDropdown -> {
                sendUiEvent(UiEvent.Dropdown(isOpen = event.isOpen))
            }
            is CreateReminderEvents.ToggleCheckBox -> {
                sendUiEvent(UiEvent.CheckBox(isChecked = event.isChecked))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}
