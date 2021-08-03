package com.george.news.network

import com.george.news.network.NetworkApi
import com.george.news.network.NewsResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository to use by request
 */
class NetworkRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getNews(
        country: String?,
        category: String?,
        apiKey: String?
    ): Response<NewsResponse> = networkApi.getNews(country, category, apiKey)
}