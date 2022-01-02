package com.atitienei_daniel.reeme.ui.screens.edit_reminder

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.RemindersDataSource
import com.atitienei_daniel.reeme.domain.model.Reminder
import com.atitienei_daniel.reeme.ui.theme.Orange900
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.atitienei_daniel.reeme.ui.utils.enums.ReminderRepeatTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import remindersdb.ReminderEntity
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditReminderViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: RemindersDataSource
) : ViewModel() {

    private var _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    var reminderId by mutableStateOf(0L)

    var title by mutableStateOf("")

    var description by mutableStateOf<String?>(null)

    var isPinned by mutableStateOf(false)

    var isDone by mutableStateOf(false)

    var date by mutableStateOf(Calendar.getInstance())

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
                date = reminder.date
                repeat = reminder.repeat
            }

            Log.d("reminder_color", (color == Orange900).toString())
        }
    }

    fun onEvent(event: EditReminderEvents) {
        when (event) {
            is EditReminderEvents.OnCloseClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }
            is EditReminderEvents.OnCreateCategoryAlertClick -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = true))
            }
            is EditReminderEvents.OnCreateCategoryAlertDismiss -> {
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
                            date = date,
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
                            date = date,
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