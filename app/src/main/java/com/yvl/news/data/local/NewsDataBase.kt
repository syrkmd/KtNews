package com.yvl.news.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ArticleDbModel::class, SubscriptionsDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class NewsDataBase: RoomDatabase() {

    abstract fun newsDao(): NewsDao
}