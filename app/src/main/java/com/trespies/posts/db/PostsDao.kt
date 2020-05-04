package com.trespies.posts.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>)

    @Query("SELECT * FROM Post ORDER BY id ASC")
    fun loadPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM User WHERE id = :userID LIMIT 1")
    fun loadUser(userID: Int): LiveData<User>

    @Query("SELECT * FROM Comment WHERE postId = :postID ORDER BY id ASC")
    fun loadCommentsByPostID(postID: Int) : LiveData<List<Comment>>

    @Query("DELETE FROM Post")
    fun deletePosts()

    @Query("DELETE FROM Comment WHERE postId = :postID")
    fun deleteCommentsByPostID(postID: Int)


}