package com.yvl.news.domain.usecase

import com.yvl.news.domain.entity.Settings
import com.yvl.news.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}