package com.atitienei_daniel.reeme.ui.screens.edit_reminder

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.domain.repository.StoreCategoriesRepository
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remindersdb.ReminderEntity
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RemindersDataSource,
    private val storeCategoriesRepositoryImpl: StoreCategoriesRepository,
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    var reminderId by mutableStateOf(0L)

    var title by mutableStateOf("")

    var description by mutableStateOf<String?>(null)

    var isPinned by mutableStateOf(false)

    var isDone by mutableStateOf(false)

    var calendar by mutableStateOf<Calendar>(Calendar.getInstance())

    var repeat by mutableStateOf(ReminderRepeatTypes.UNSELECTED)

    var selectedCategories = mutableStateListOf<String>()

    var color by mutableStateOf<Color?>(null)

    init {
        savedStateHandle.get<String>("reminderId")?.let { reminderId ->
            getReminderById(id = reminderId)
        }
    }

    private fun getReminderById(id: String) {
        viewModelScope.launch {
            val reminder = repository.getReminderById(id = id.toLong())

            withContext(Dispatchers.Main) {
                reminderId = reminder?.id!!
                title = reminder.title
                description = reminder.description
                isPinned = reminder.isPinned
                isDone = reminder.isDone
                color = reminder.color
                reminder.categories!!.forEach { category ->
                    selectedCategories.add(category)
                }
                calendar.time = reminder.date
                repeat = reminder.repeat
            }
        }
    }

    val categories = storeCategoriesRepositoryImpl.getCategories

    private fun insertCategory(categories: MutableList<String>) {
        viewModelScope.launch {
            storeCategoriesRepositoryImpl.insertCategory(categories = categories)
        }
    }


    fun onEvent(event: EditReminderEvents) {
        when (event) {
            is EditReminderEvents.OnDeleteClick -> {
                viewModelScope.launch {
                    repository.deleteReminderById(reminderId)
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
            is EditReminderEvents.OpenCreateCategoryAlertDialog -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = true))
            }
            is EditReminderEvents.DismissCreateCategoryAlert -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = false))
            }
            is EditReminderEvents.OpenDatePicker -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = true))
            }
            is EditReminderEvents.DismissDatePicker -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = false))
            }
            is EditReminderEvents.OpenTimePicker -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = true))
            }
            is EditReminderEvents.DismissTimePicker -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = false))
            }
            is EditReminderEvents.ToggleDropdown -> {
                sendUiEvent(UiEvent.Dropdown(isOpen = event.isOpen))
            }
            is EditReminderEvents.ToggleCheckBox -> {
                isPinned = event.isChecked
            }
            is EditReminderEvents.InsertCategory -> {
                insertCategory(event.categories)
                sendUiEvent(UiEvent.AlertDialog(isOpen = false))
            }
            is EditReminderEvents.OnDoneClick -> {
                isDone = !isDone
                viewModelScope.launch {
                    repository.updateReminder(
                        reminder = ReminderEntity(
                            id = reminderId,
                            isDone = isDone,
                            isPinned = isPinned,
                            repeat = repeat,
                            date = calendar.time,
                            color = color!!,
                            description = description,
                            categories = selectedCategories,
                            title = title
                        )
                    )
                }
            }
            is EditReminderEvents.OnSaveClick -> {
                viewModelScope.launch {
                    repository.updateReminder(
                        reminder = ReminderEntity(
                            id = reminderId,
                            isDone = isDone,
                            isPinned = isPinned,
                            repeat = repeat,
                            date = calendar.time,
                            color = color!!,
                            description = description,
                            categories = selectedCategories,
                            title = title
                        )
                    )
                }
                sendUiEvent(UiEvent.PopBackStack)
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }
}