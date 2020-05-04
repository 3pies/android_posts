package com.trespies.posts.repository

import androidx.lifecycle.LiveData
import com.trespies.posts.AppExecutors
import com.trespies.posts.api.PostService
import com.trespies.posts.api.objects.ApiObjectUser
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.User
import com.trespies.posts.repository.objects.build
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OpenForTesting
class UserRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val postDao: PostsDao,
    private val apiRestService: PostService
) {

    fun loadUser(userId: Int): LiveData<Resource<User>> {
        return object : NetworkBoundResource<User, ApiObjectUser>(appExecutors) {
            override fun saveCallResult(item: ApiObjectUser) {

                User.build(item)?.let {
                    postDao.insertUser(it)
                }

            }

            override fun shouldFetch(data: User?) = true

            override fun loadFromDb() = postDao.loadUser(userId)

            override fun createCall() = apiRestService.getUser(userId)

            override fun onFetchFailed() {

            }

        }.asLiveData()
    }

}