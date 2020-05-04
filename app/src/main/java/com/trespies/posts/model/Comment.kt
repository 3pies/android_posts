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
    companion object
}