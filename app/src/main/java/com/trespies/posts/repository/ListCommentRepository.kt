package com.trespies.posts.repository

import androidx.lifecycle.LiveData
import com.trespies.posts.AppExecutors
import com.trespies.posts.api.PostService
import com.trespies.posts.api.objects.ApiObjectComment
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.Comment
import com.trespies.posts.repository.objects.build
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class ListCommentRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val postDao: PostsDao,
    private val apiRestService: PostService
) {

    fun loadComments(postID: Int): LiveData<Resource<List<Comment>>> {
        return object : NetworkBoundResource<List<Comment>, List<ApiObjectComment>>(appExecutors) {
            override fun saveCallResult(item: List<ApiObjectComment>) {
                val list = mutableListOf<Comment>()
                item.forEach { apiObject ->
                    Comment.build(apiObject)?.let { comment -> list.add(comment) }
                }

                postDao.deleteCommentsByPostID(postID)
                postDao.insertComments(list)
            }

            override fun shouldFetch(data: List<Comment>?) = true

            override fun loadFromDb() = postDao.loadCommentsByPostID(postID)

            override fun createCall() = apiRestService.getCommentsByPostID(postID)

            override fun onFetchFailed() {

            }

        }.asLiveData()
    }

}