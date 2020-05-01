package com.trespies.posts.model

import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
    indices = [
        Index("userId")
    ],
    primaryKeys = ["id"]
)
data class Post (val id: Int, val userId: Int, val title: String?, val body: String?) : Serializable