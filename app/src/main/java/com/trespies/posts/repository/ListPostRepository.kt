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
class ListPostRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val postDao: PostsDao,
    private val apiRestService: PostService
) {

    fun loadPosts(): LiveData<Resource<List<Post>>> {
        return object : NetworkBoundResource<List<Post>, List<ApiObjectPost>>(appExecutors) {
            override fun saveCallResult(item: List<ApiObjectPost>) {
                val list = mutableListOf<Post>()
                item.forEach { apiObject ->
                    Post.build(apiObject)?.let { post -> list.add(post) }
                }

                postDao.deletePosts()
                postDao.insertPosts(list)
            }

            override fun shouldFetch(data: List<Post>?) = true

            override fun loadFromDb() = postDao.loadPosts()

            override fun createCall() = apiRestService.getPosts()

            override fun onFetchFailed() {

            }

        }.asLiveData()
    }

}