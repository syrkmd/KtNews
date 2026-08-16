package com.yvl.news.domain.usecase

import com.yvl.news.domain.repository.NewsRepository
import javax.inject.Inject

class UpdateSubscribedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke() {
        newsRepository.updateArticlesForAllSubscriptions()
    }
}