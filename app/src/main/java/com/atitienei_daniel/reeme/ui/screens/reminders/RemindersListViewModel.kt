package com.atitienei_daniel.reeme.ui.screens.reminders

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.ui.utils.Routes
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class RemindersListViewModel @Inject constructor(
    private val repository: RemindersDataSource,
    private val moshi: Moshi
) : ViewModel() {

    val reminders = repository.getReminders()

    var isFilterOpened by mutableStateOf(false)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onEvent(event: RemindersListEvents) {
        when (event) {
            is RemindersListEvents.onReminderClick -> {
                sendUiEvent(
                    UiEvent.Navigate(
                        Routes.EDIT_REMINDER.replace(
                            "{reminderId}",
                            event.reminder.id.toString()
                        )
                    )
                )
            }
            is RemindersListEvents.onAddClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CREATE_REMINDER))
            }
            is RemindersListEvents.onSearchClick -> {
                sendUiEvent(UiEvent.BackDropScaffold)
            }
            is RemindersListEvents.onFilterClick -> {
                isFilterOpened = !isFilterOpened
            }
            is RemindersListEvents.onSettingsClick -> {

            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}