package com.trespies.posts.util

import com.trespies.posts.api.objects.ApiObjectComment
import com.trespies.posts.api.objects.ApiObjectPost
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User

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

    fun createUser(id: Int, name: String?, email: String?) = User(
        id, name, name, name, email, "phone", "website"
    )

    fun createComment(id: Int, postID: Int, name: String?, email: String?, body: String?) = Comment(
        id, postID, name, email, body
    )

    fun createApiComment(id: Int, postId: Int, name: String?, email: String?, body: String?) = ApiObjectComment(
        id = id,
        postId = postId,
        name = name,
        email = email,
        body = body
    )
}
