package com.george.news.news_fragment

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.george.news.network.NetworkRepository
import com.george.news.getAPIKey
import com.george.news.network.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val networkRepository: NetworkRepository
) :
    AndroidViewModel(application) {

    var postListNews: LiveData<PagedList<Articles>>? = null

    private val configNews = PagedList.Config.Builder()
        .setPageSize(5)
        .setInitialLoadSizeHint(5)
        .setEnablePlaceholders(false)
        .build()

    init {
        // Method to use with PagedList
        fetchNews("us", "business", getAPIKey())
    }

    private fun fetchNews(country: String, category: String, apiKey: String) {
        postListNews =
            initializedPagedListBuilderNews(configNews, country, category, apiKey).build()
    }

    private fun initializedPagedListBuilderNews(
        config: PagedList.Config,
        country: String,
        category: String,
        apiKey: String
    ):
            LivePagedListBuilder<Int, Articles> {
        val dataSourceFactory =
            NewsDataSourceFactory(networkRepository, viewModelScope, country, category, apiKey)
        return LivePagedListBuilder(dataSourceFactory, config)
    }
}