package com.george.news.news_fragment

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.george.news.network.NetworkRepository
import com.george.news.getAPIKey
import com.george.news.network.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    application: Application,
    private val networkRepository: NetworkRepository
) :
    AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<NewsApiStatus>()
    // The external immutable LiveData for the request status
    val status: LiveData<NewsApiStatus> = _status

    // Internally, we use a MutableLiveData, because we will be updating the List of NewsData
    // with new values
    private val _newsData = MutableLiveData<Resource<NewsResponse>>()
    // The external LiveData interface to the property is immutable, so only this class can modify
    val newsData: LiveData<Resource<NewsResponse>> = _newsData

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

    private fun fetchNews(country: String, category:String, apiKey:String) {
        //loading.postValue(true)
        postListNews = initializedPagedListBuilderNews(configNews, country, category, apiKey).build()
    }

    private fun initializedPagedListBuilderNews(config: PagedList.Config, country: String, category:String, apiKey:String):
            LivePagedListBuilder<Int, Articles> {
        val dataSourceFactory = NewsDataSourceFactory(networkRepository, viewModelScope, country, category, apiKey)
        return LivePagedListBuilder(dataSourceFactory, config)
    }

}