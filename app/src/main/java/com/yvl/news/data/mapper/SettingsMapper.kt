package com.yvl.news.data.mapper

import com.yvl.news.domain.entity.RefreshConfig
import com.yvl.news.domain.entity.Settings

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(language, interval, wifiOnly)
}