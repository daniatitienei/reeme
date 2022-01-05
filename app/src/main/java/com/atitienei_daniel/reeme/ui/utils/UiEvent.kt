package com.atitienei_daniel.reeme.ui.utils

sealed class UiEvent {
    object PopBackStack : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class ShowSnackBar(
        val message: String,
        val action: String? = null
    ) : UiEvent()

    object BackDropScaffold : UiEvent()
    data class AlertDialog(val isOpen: Boolean = false): UiEvent()
    data class DatePicker(val isOpen: Boolean = false): UiEvent()
    data class TimePicker(val isOpen: Boolean = false): UiEvent()
    data class Dropdown(val isOpen: Boolean = false): UiEvent()
    data class CheckBox(val isChecked: Boolean = false): UiEvent()
}
