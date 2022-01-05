package com.atitienei_daniel.reeme.ui.screens.reminders

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.data.datastore.StoreCategories
import com.atitienei_daniel.reeme.data.reminders_db.RemindersDataSource
import com.atitienei_daniel.reeme.ui.utils.Routes
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class RemindersListViewModel @Inject constructor(
    private val repository: RemindersDataSource,
    private val moshi: Moshi,
    private val storeCategories: StoreCategories,
) : ViewModel() {

    val reminders = repository.getReminders()

    var isFilterOpened by mutableStateOf(false)

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val categories = storeCategories.getCategories

    fun onEvent(event: RemindersListEvents) {
        when (event) {
            is RemindersListEvents.OnReminderClick -> {
                sendUiEvent(
                    UiEvent.Navigate(
                        Routes.EDIT_REMINDER.replace(
                            "{reminderId}",
                            event.reminder.id.toString()
                        )
                    )
                )
            }
            is RemindersListEvents.OnAddClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.CREATE_REMINDER))
            }
            is RemindersListEvents.OnSearchClick -> {
                sendUiEvent(UiEvent.BackDropScaffold)
            }
            is RemindersListEvents.OnFilterClick -> {
                isFilterOpened = !isFilterOpened
            }
            is RemindersListEvents.OnSettingsClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.SETTINGS))
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }
}