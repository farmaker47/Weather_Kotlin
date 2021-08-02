package com.george.news.network

import androidx.paging.DataSource
import com.george.news.di.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class NewsDataSourceFactory @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val coroutineScope: CoroutineScope,
    private val country: String,
    private val category: String,
    private val apiKey: String,
    val closure: ((Int) -> Unit)? = null
) : DataSource.Factory<Int, Articles>() {

    override fun create(): DataSource<Int, Articles> {
        return NewsDataSource(
            networkRepository,
            coroutineScope,
            country,
            category,
            apiKey,
            closure
        )
    }

}