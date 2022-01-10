package com.atitienei_daniel.reeme.ui.screens.settings

import com.atitienei_daniel.reeme.domain.model.Feedback
import com.atitienei_daniel.reeme.ui.utils.enums.Theme

sealed class SettingsEvents {
    data class OnChangeThemeClick(val theme: Theme): SettingsEvents()
    object OnNavigationIconClick: SettingsEvents()
    data class SendFeedback(val text: String): SettingsEvents()
    data class ToggleFeedbackAlert(val isOpen: Boolean): SettingsEvents()
}