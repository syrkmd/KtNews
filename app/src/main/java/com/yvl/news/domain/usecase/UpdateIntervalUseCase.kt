package com.yvl.news.domain.usecase

import com.yvl.news.domain.entity.Interval
import com.yvl.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateIntervalUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(interval: Interval) {
        repository.updateInterval(interval.minutes)
    }
}