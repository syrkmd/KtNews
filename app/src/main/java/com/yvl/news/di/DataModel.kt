package com.yvl.news.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.yvl.news.data.local.NewsDao
import com.yvl.news.data.local.NewsDataBase
import com.yvl.news.data.remote.NewsApiService
import com.yvl.news.data.repository.NewsRepositoryImpl
import com.yvl.news.data.repository.SettingsRepositoryImpl
import com.yvl.news.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModel {

    @Singleton
    @Binds
    fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    @Singleton
    @Binds
    fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepositoryImpl

    companion object {

        @Singleton
        @Provides
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager = WorkManager.getInstance(context)

        @Singleton
        @Provides
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }
        }

        @Singleton
        @Provides
        fun provideConverterFactory(
            json: Json
        ): Converter.Factory {
            return json.asConverterFactory(
                "application/json".toMediaType()
            )
        }

        @Singleton
        @Provides
        fun provideRetrofit(
            converterFactory: Converter.Factory,
        ): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(converterFactory)
                .build()
        }

        @Singleton
        @Provides
        fun provideApiService(
            retrofit: Retrofit
        ): NewsApiService {
            return retrofit.create()
        }

        @Singleton
        @Provides
        fun provideDataBase(
            @ApplicationContext context: Context
        ): NewsDataBase {
            return Room.databaseBuilder(
                context = context,
                klass = NewsDataBase::class.java,
                name = "news.db"
            ).fallbackToDestructiveMigration(dropAllTables = true).build()
        }

        @Singleton
        @Provides
        fun provideNewsDao(
            dataBase: NewsDataBase
        ): NewsDao {
            return dataBase.newsDao()
        }
    }
}