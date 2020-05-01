package com.trespies.posts.repository.objects

import com.trespies.posts.api.objects.ApiObjectPost
import com.trespies.posts.model.Post

inline fun Post.Companion.build (item: ApiObjectPost?) : Post? {
    val obj = item ?: return null
    return Post(obj.id, obj.userId, obj.title, obj.body)
}