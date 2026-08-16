package com.yvl.news.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionsDbModel(@PrimaryKey val topic: String)
