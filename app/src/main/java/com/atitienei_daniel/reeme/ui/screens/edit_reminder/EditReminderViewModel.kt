package com.atitienei_daniel.reeme.ui.screens.edit_reminder

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.datastore.StoreCategories
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.dateToString
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remindersdb.ReminderEntity
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RemindersDataSource,
    private val storeCategories: StoreCategories
) : ViewModel() {

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var reminderId by mutableStateOf(0L)

    var title by mutableStateOf("")

    var description by mutableStateOf<String?>(null)

    var isPinned by mutableStateOf(false)

    var isDone by mutableStateOf(false)

    var date by mutableStateOf<Calendar>(Calendar.getInstance())

    var repeat by mutableStateOf(ReminderRepeatTypes.UNSELECTED)

    var selectedCategories = mutableStateListOf<String>()

    var color by mutableStateOf<Color?>(null)

    init {
        savedStateHandle.get<String>("reminderId")?.let { reminderId ->
            getReminderById(id = reminderId)
        }

        getCategories()
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
                date.time = reminder.date
                repeat = reminder.repeat
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            storeCategories.getCategories.collect {
                Log.d("reminder_categories", it.toString())
            }
        }
    }

    fun insertCategory() {
        viewModelScope.launch {
            storeCategories.getCategories.collect { categories ->
                categories?.let {
                    storeCategories.insertCategory(categories = it)
                }
            }
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
            is EditReminderEvents.OnCreateCategoryClick -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = true))
            }
            is EditReminderEvents.OnCreateCategoryAlertDismiss -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = false))
            }
            is EditReminderEvents.OnSelectDateClick -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = true))
            }
            is EditReminderEvents.OnSelectDateDismiss -> {
                sendUiEvent(UiEvent.DatePicker(isOpen = false))
            }
            is EditReminderEvents.OnSelectTimeClick -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = true))
            }
            is EditReminderEvents.OnSelectTimeDismiss -> {
                sendUiEvent(UiEvent.TimePicker(isOpen = false))
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
                            date = date!!.time,
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
                            date = date!!.time,
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
            _uiEvent.send(uiEvent)
        }
    }
}