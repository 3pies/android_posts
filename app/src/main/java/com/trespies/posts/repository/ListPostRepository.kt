package com.trespies.posts.repository

import com.trespies.posts.AppExecutors
import com.trespies.posts.api.PostService
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.testing.OpenForTesting
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class ListPostRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: PostsDB,
    private val postDao: PostsDao,
    private val apiRestService: PostService
) {

}