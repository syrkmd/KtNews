package com.yvl.news.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yvl.news.domain.entity.Interval
import com.yvl.news.domain.entity.Language
import com.yvl.news.domain.usecase.GetSettingsUseCase
import com.yvl.news.domain.usecase.UpdateIntervalUseCase
import com.yvl.news.domain.usecase.UpdateLanguageUseCase
import com.yvl.news.domain.usecase.UpdateNotificationsEnabledUseCase
import com.yvl.news.domain.usecase.UpdateWifiOnlyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val updateIntervalUseCase: UpdateIntervalUseCase,
    private val updateNotificationsEnabledUseCase: UpdateNotificationsEnabledUseCase,
    private val updateWifiOnlyUseCase: UpdateWifiOnlyUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SettingsState>(SettingsState.Initial)
    val state = _state.asStateFlow()

    init {
        observeSettings()
    }

    fun processCommand(command: SettingsCommand) {
        viewModelScope.launch {
            when (command) {
                is SettingsCommand.SelectInterval -> {
                    updateIntervalUseCase(command.interval)
                }
                is SettingsCommand.SelectLanguage -> {
                    updateLanguageUseCase(command.language)
                }
                is SettingsCommand.SwitchNotificationEnabled -> {
                    updateNotificationsEnabledUseCase(command.enabled)
                }
                is SettingsCommand.SwitchUpdateOnlyWifi -> {
                    updateWifiOnlyUseCase(command.wifiOnly)
                }
            }
        }
    }

    private fun observeSettings() {
        getSettingsUseCase()
            .onEach { settings ->
                _state.update {
                    SettingsState.Configuration(
                        language = settings.language,
                        interval = settings.interval,
                        notificationEnabled = settings.notificationEnabled,
                        wifiOnly = settings.wifiOnly
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}

sealed interface SettingsCommand {

    data class SelectLanguage(val language: Language) : SettingsCommand

    data class SelectInterval(val interval: Interval) : SettingsCommand

    data class SwitchNotificationEnabled(val enabled: Boolean) : SettingsCommand

    data class SwitchUpdateOnlyWifi(val wifiOnly: Boolean) : SettingsCommand
}


sealed interface SettingsState {

    data object Initial : SettingsState

    data class Configuration(
        val language: Language,
        val interval: Interval,
        val notificationEnabled: Boolean,
        val wifiOnly: Boolean,
        val languages: List<Language> = Language.entries,
        val intervals: List<Interval> = Interval.entries
    ) : SettingsState
}

