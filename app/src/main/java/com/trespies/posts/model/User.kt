package com.trespies.posts.model

import androidx.room.Entity

@Entity(
    primaryKeys = ["id"]
)
data class User (val id : Int, val name: String?, val surname: String?,
                 val username: String?, val email: String?,
                 val phone: String?, val website: String?) {
    companion object
}