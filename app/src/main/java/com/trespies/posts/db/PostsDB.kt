package com.trespies.posts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User


/**
 * Main database description.
 */
@Database(
        entities = [
            Post::class,
            User::class,
            Comment::class
        ],
        version = 2,
        exportSchema = false
)
abstract class PostsDB : RoomDatabase() {

    abstract fun postsDAO(): PostsDao

}