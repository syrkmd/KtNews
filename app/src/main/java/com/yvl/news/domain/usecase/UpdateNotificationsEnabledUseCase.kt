package com.yvl.news.domain.usecase

import com.yvl.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateNotificationsEnabledUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(enabled: Boolean) {
        repository.updateNotificationsEnabled(enabled)
    }
}