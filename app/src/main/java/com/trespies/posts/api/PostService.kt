package com.trespies.posts.api

import androidx.lifecycle.LiveData
import com.trespies.posts.api.objects.ApiObjectPost
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * REST API access point
 */
interface PostService {

    @GET("posts")
    fun getPosts(): LiveData<ApiResponse<List<ApiObjectPost>>>
}