package com.atitienei_daniel.reeme.ui.screens.settings

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atitienei_daniel.reeme.domain.repository.StoreThemeRepository
import com.atitienei_daniel.reeme.ui.utils.UiEvent
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: StoreThemeRepository,
    private val firestore: FirebaseFirestore,
) : ViewModel() {

    private var _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val theme = repository.getTheme

    fun onEvent(event: SettingsEvents) {
        when (event) {
            is SettingsEvents.OnChangeThemeClick -> {
                viewModelScope.launch {
                    repository.setTheme(event.theme)
                }
            }
            is SettingsEvents.OnNavigationIconClick -> {
                sendUiEvent(UiEvent.PopBackStack)
            }
            is SettingsEvents.SendFeedback -> {
                firestore.collection("feedback")
                    .add(
                        hashMapOf("text" to event.text)
                    ).addOnSuccessListener { documentReference ->
                        Log.d("feedback", "DocumentSnapshot written with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("feedback", "Error adding document", e)
                    }
                sendUiEvent(UiEvent.AlertDialog(isOpen = false))
            }
            is SettingsEvents.ToggleFeedbackAlert -> {
                sendUiEvent(UiEvent.AlertDialog(isOpen = event.isOpen))
            }
        }
    }

    private fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(uiEvent)
        }
    }
}