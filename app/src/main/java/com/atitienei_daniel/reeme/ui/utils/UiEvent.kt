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
}
