package com.yvl.news.domain.usecase

import com.yvl.news.domain.entity.Language
import com.yvl.news.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val repository: SettingsRepository
) {

    suspend operator fun invoke(language: Language) {
        repository.updateLanguage(language)
    }
}