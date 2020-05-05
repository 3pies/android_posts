package com.trespies.posts.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.trespies.posts.util.LiveDataCallAdapterFactory
import com.trespies.posts.util.getOrAwaitValue
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(JUnit4::class)
class GithubServiceTest {
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: PostService

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun createService() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(PostService::class.java)
    }

    @After
    fun stopService() {
        mockWebServer.shutdown()
    }

    @Test
    fun getPosts() {
        enqueueResponse("posts.json")
        val posts = (service.getPosts().getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/posts"))

        assertThat(posts.size, `is`(3))

        val post = posts[0]

        assertThat(post.id, `is`(1))
        assertThat(post.userId, `is`(1))
        assertThat(post.title, `is`("title1"))
        assertThat(post.body, `is`("body1"))

        val post2 = posts[1]
        assertThat(post2.id, `is`(2))
        assertThat(post2.userId, `is`(1))

        val post3 = posts[2]
        assertThat(post3.id, `is`(3))
        assertThat(post3.userId, `is`(2))
    }

    @Test
    fun getPostDetail() {
        enqueueResponse("post_detail.json")

        val post = (service.getPost(1).getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/posts/1"))

        assertThat(post, notNullValue())
        assertThat(post.id, `is`(1))
        assertThat(post.userId, `is`(2))
        assertThat(post.title, `is`("title1"))
        assertThat(post.body, `is`("body1"))
    }

    @Test
    fun getUserDetail() {
        enqueueResponse("user_detail.json")

        val user = (service.getUser(1).getOrAwaitValue() as ApiSuccessResponse).body

        val request = mockWebServer.takeRequest()
        assertThat(request.path, `is`("/users/1"))

        assertThat(user, notNullValue())
        assertThat(user.id, `is`(1))
        assertThat(user.email, `is`("daniel.ferrer.delgado@gmail.com"))
        assertThat(user.name, `is`("Daniel Ferrer"))
        assertThat(user.username, `is`("ferrer"))
        assertThat(user.company?.name, `is`("3pies.es"))
        assertThat(user.address?.city, `is`("coruña"))
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api-response/$fileName")
        val source = Okio.buffer(Okio.source(inputStream))
        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockWebServer.enqueue(
            mockResponse
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}