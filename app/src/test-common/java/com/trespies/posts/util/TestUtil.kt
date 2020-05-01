package com.trespies.posts.util

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


}
