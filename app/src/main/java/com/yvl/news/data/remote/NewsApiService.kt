package com.yvl.news.data.remote

import com.yvl.news.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=${BuildConfig.NEWS_API_KEY}")
    suspend fun loadArticles(
        @Query("q") topic: String,
        @Query("pageSize") pageSize: Int = 25,
        @Query("language") language: String
    ): NewsResponseDto
}