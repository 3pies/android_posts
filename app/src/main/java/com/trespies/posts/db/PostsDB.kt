package com.trespies.posts.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.trespies.posts.model.Post


/**
 * Main database description.
 */
@Database(
        entities = [
            Post::class

        ],
        version = 1,
        exportSchema = false
)
abstract class PostsDB : RoomDatabase() {

    abstract fun postsDAO(): PostsDao

}