package com.trespies.posts.repository.objects

import com.trespies.posts.api.objects.ApiObjectUser
import com.trespies.posts.model.User

inline fun User.Companion.build(item: ApiObjectUser?): User? {
    val item = item ?: return null
    return User(item.id, item.name, item.surname, item.username, item.email, item.phone, item.website)
}