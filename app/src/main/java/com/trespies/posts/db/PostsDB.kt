package com.trespies.posts.db

import androidx.room.Database
import androidx.room.RoomDatabase



/**
 * Main database description.
 */
@Database(
        entities = [


        ],
        version = 1,
        exportSchema = false
)
abstract class PostsDB : RoomDatabase() {

    abstract fun postsDAO(): PostsDao

}