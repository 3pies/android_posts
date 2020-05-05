package com.trespies.posts.repository

import androidx.lifecycle.LiveData
import com.trespies.posts.AppExecutors
import com.trespies.posts.api.PostService
import com.trespies.posts.api.objects.ApiObjectPost
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.Post
import com.trespies.posts.repository.objects.build
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class PostRepository  @Inject constructor(
    private val appExecutors: AppExecutors,
    private val postDao: PostsDao,
    private val apiRestService: PostService
) {

    fun loadPost(postID: Int): LiveData<Resource<Post>> {
        return object : NetworkBoundResource<Post, ApiObjectPost>(appExecutors) {
            override fun saveCallResult(item: ApiObjectPost) {
                Post.build(item)?.let { post ->
                    postDao.insertPosts(listOf(post))
                }
            }

            override fun shouldFetch(data: Post?) = true

            override fun loadFromDb() = postDao.loadPost(postID)

            override fun createCall() = apiRestService.getPost(postID)

            override fun onFetchFailed() {

            }

        }.asLiveData()
    }

}