package com.yvl.news.domain.usecase

import com.yvl.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateWifiOnlyUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(wifiOnly: Boolean) {
        repository.updateWifiOnly(wifiOnly)
    }
}