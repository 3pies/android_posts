package com.trespies.posts.util

import com.trespies.posts.api.objects.ApiObjectPost
import com.trespies.posts.model.Post

object TestUtil {

    fun createPosts(count: Int, userId: Int, title: String?, body: String?): List<Post> {
        return (0 until count).map {
            createPost(
                id = it,
                userId = userId,
                title = title,
                body = body
            )
        }
    }

    fun createPost(id: Int, userId: Int, title: String?, body: String?) = Post(
        id = id,
        userId = userId,
        title = title,
        body = body
    )

    fun createApiPosts(count: Int, userId: Int, title: String?, body: String?): List<ApiObjectPost> {
        return (0 until count).map {
            createApiPost(
                id = it,
                userId = userId,
                title = title,
                body = body
            )
        }
    }

    fun createApiPost(id: Int, userId: Int, title: String?, body: String?) = ApiObjectPost(
        id = id,
        userId = userId,
        title = title,
        body = body
    )


}