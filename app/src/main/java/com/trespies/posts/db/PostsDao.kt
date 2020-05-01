package com.trespies.posts.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trespies.posts.model.Post

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)

    @Query("SELECT * FROM Post ORDER BY id ASC")
    fun loadPosts(): LiveData<List<Post>>

    @Query("DELETE FROM Post")
    fun deletePosts()


}