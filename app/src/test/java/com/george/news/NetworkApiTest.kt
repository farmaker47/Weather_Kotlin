package com.george.news

import com.george.news.network.NetworkApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class NetworkApiTest {

    // Subject under test
    private lateinit var apiService: NetworkApi

    // A scriptable web server for testing HTTP clients. Callers supply canned
    // responses and the server replays them upon request in sequence.
    private lateinit var mockServer: MockWebServer

    @Before
    fun createService() {
        mockServer = MockWebServer()

        apiService = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkApi::class.java)
    }

    @After
    fun stopService() {
        mockServer.shutdown()
    }

    @Test
    fun getNewsDetails() {
        runBlocking {
            mockServer.enqueueMockResponse("articles.json", 200)
            val response = apiService.getNews("us","country", "some_key")

            // Verify that the endpoint contains the movie ID
            val request = mockServer.takeRequest()
            assertThat(request.requestUrl, notNullValue())
            assertThat(request.path, containsString("country"))

            // Verify that the response contains the expected movie details
            val newsResponse = response.body()

            assertThat(newsResponse, notNullValue())
            assertThat(newsResponse?.totalResults.toString(), `is`("70"))
            assertThat(newsResponse!!.articles[0].author, `is`("Kevin Stankiewicz"))
            assertThat(newsResponse.articles[0].publishedAt, `is`("2021-08-04T00:30:00Z"))
        }
    }

    private fun MockWebServer.enqueueMockResponse(fileName: String, code: Int) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("response/$fileName")
        val source = inputStream?.let {
            inputStream.source().buffer()
        }

        source?.let {
            val mockResponse = MockResponse()
                .setResponseCode(code)
                .setBody(source.readString(Charsets.UTF_8))
            enqueue(mockResponse)
        }
    }
}