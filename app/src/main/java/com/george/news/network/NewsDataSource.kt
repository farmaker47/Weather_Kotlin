package com.george.news.network

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NewsDataSource @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val coroutineScope: CoroutineScope,
    private val country: String,
    private val category: String,
    private val apiKey: String,
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
                        apiKey
                    )
                }
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    response.body()?.articles.let {
                        it?.let { it1 ->
                            callback.onResult(
                                it1,
                                null,
                                1
                            )
                        }
                    }
                    response.body()?.articles?.count()?.let { closure?.invoke(it) }
                    listSize.postValue(response.body()?.articles?.count())
                }

            }.onFailure {
                closure?.invoke(0)
                listSize.postValue(0)
            }
        }
    }

    companion object {
        // Value to observe in NewsFragment
        var listSize: MutableLiveData<Int> = MutableLiveData(-1)
    }

    // Implement below if you have more than one page
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Articles>) {}

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Articles>) {}
}