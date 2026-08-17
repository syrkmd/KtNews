package com.yvl.news.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/everything?apiKey=0e41606e807947a0b293ecab87935b1a")
    suspend fun loadArticles(
        @Query("q") topic: String,
        @Query("pageSize") pageSize: Int = 25,
        @Query("language") language: String
    ): NewsResponseDto
}