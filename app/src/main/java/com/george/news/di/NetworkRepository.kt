package com.george.news.di

import com.george.news.network.NetworkApi
import com.george.news.network.NewsResponse
import retrofit2.Response
import javax.inject.Inject

class NetworkRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    suspend fun getNews(
        country: String?,
        category: String?,
        apiKey: String?
    ): Response<NewsResponse> = networkApi.getNews(country, category, apiKey)
}