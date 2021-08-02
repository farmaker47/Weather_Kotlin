package com.george.news.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.george.news.di.NetworkRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsDataSource @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val coroutineScope: CoroutineScope,
    private val country: String,
    private val category:String,
    private val apiKey:String,
    val closure: ((Int) -> Unit)? = null
) : PageKeyedDataSource<Int, Articles>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Articles>
    ) {
        coroutineScope.launch(Dispatchers.Main) {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    networkRepository.getNews(
                        country,
                        category,
                        apiKey)
                }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.articles.let { it?.let { it1 -> callback.onResult(it1, null, 1) } }
                    response.body()?.articles?.count()?.let { closure?.invoke(it) }
                }

            }.onFailure {
                closure?.invoke(0)
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Articles>) {

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Articles>) {

    }
}