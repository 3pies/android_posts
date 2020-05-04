package com.trespies.posts.repository.objects

import com.trespies.posts.api.objects.ApiObjectComment
import com.trespies.posts.model.Comment

inline fun Comment.Companion.build(item: ApiObjectComment?) : Comment? {
    val item = item ?: return null
    return Comment(item.id, item.postId, item.name, item.email, item.body)
}