package com.trespies.posts.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    indices = [
        Index("postId")
    ],
    primaryKeys = ["id"]
)
data class Comment (val id: Int, val postId: Int, val name: String?, val email: String?, val body: String?) {

    val avatarUrl: String?
        get() {
            val key = email ?: return null
            return "https://api.adorable.io/avatars/150/${key}"
        }

    companion object
}