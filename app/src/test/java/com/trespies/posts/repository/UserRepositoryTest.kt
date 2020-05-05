package com.trespies.posts.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trespies.posts.api.PostService
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.User
import com.trespies.posts.util.*
import com.trespies.posts.vo.Resource
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

@RunWith(JUnit4::class)
class UserRepositoryTest {

    private lateinit var repository: UserRepository
    private val dao = Mockito.mock(PostsDao::class.java)
    private val service = Mockito.mock(PostService::class.java)
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = Mockito.mock(PostsDB::class.java)
        Mockito.`when`(db.postsDAO()).thenReturn(dao)
        Mockito.`when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = UserRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadUser() {
        val dbData = MutableLiveData<User>()
        Mockito.`when`(dao.loadUser(1)).thenReturn(dbData)

        val data = repository.loadUser(1)
        Mockito.verify(dao).loadUser(1)

        Mockito.verify(service, Mockito.never()).getUser(1)


        val apiUser = TestUtil.createApiUser( 1, "name", "username", "email@info.co")
        // network does not send these

        val call = ApiUtil.successCall(apiUser)
        Mockito.`when`(service.getUser(1))
            .thenReturn(call)

        val observer = mock<Observer<Resource<User>>>()
        data.observeForever(observer)

        Mockito.verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<User>()
        Mockito.`when`(dao.loadUser(1)).thenReturn(updatedDbData)
        dbData.value = null

        Mockito.verify(service).getUser(1)
        val inserted = argumentCaptor<User>()
        // empty list is a workaround for null capture return
        Mockito.verify(dao).insertUser(inserted.capture() ?: User(0, null, null, null, null, null, null))

        //MatcherAssert.assertThat(inserted, ArgumentMatchers.notNull())
        val value = inserted.value
        MatcherAssert.assertThat(value.id, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(value.name, CoreMatchers.`is`("name"))
        MatcherAssert.assertThat(value.email, CoreMatchers.`is`("email@info.co"))
        MatcherAssert.assertThat(value.username, CoreMatchers.`is`("username"))

        updatedDbData.value = inserted.value
        Mockito.verify(observer).onChanged(Resource.success(inserted.value))
    }

}