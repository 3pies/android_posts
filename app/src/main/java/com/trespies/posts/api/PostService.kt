package com.trespies.posts.api

import androidx.lifecycle.LiveData
import com.trespies.posts.api.objects.ApiObjectComment
import com.trespies.posts.api.objects.ApiObjectPost
import com.trespies.posts.api.objects.ApiObjectUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


/**
 * REST API access point
 */
interface PostService {

    @GET("posts")
    fun getPosts(): LiveData<ApiResponse<List<ApiObjectPost>>>

    @GET("users/{userID}")
    fun getUser(@Path("userID") userID: Int) : LiveData<ApiResponse<ApiObjectUser>>

    @GET("posts/{postID}/comments")
    fun getCommentsByPostID(@Path("postID") postID: Int) : LiveData<ApiResponse<List<ApiObjectComment>>>
}