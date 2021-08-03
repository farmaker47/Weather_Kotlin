package com.george.news.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    /**
     * Get the top headlines from NewsApi
     * https://newsapi.org/
     */
    @GET("/v2/top-headlines")
    suspend fun getNews(
        @Query("country") country: String?,
        @Query("category") category: String?,
        @Query("apiKey") apiKey: String?
    ): Response<NewsResponse>


}