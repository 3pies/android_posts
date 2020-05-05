package com.trespies.posts.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trespies.posts.api.PostService
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.Post
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
class PostRepositoryTest {

    private lateinit var repository: PostRepository
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
        repository = PostRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadPost() {
        val dbData = MutableLiveData<Post>()
        Mockito.`when`(dao.loadPost(1)).thenReturn(dbData)

        val data = repository.loadPost(1)
        Mockito.verify(dao).loadPost(1)

        Mockito.verify(service, Mockito.never()).getPost(1)


        val apiComment = TestUtil.createApiPost( 1, 2,"title", "body")
        // network does not send these

        val call = ApiUtil.successCall(apiComment)
        Mockito.`when`(service.getPost(1))
            .thenReturn(call)

        val observer = mock<Observer<Resource<Post>>>()
        data.observeForever(observer)

        Mockito.verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<Post>()
        Mockito.`when`(dao.loadPost(1)).thenReturn(updatedDbData)
        dbData.value = null

        Mockito.verify(service).getPost(1)
        val inserted = argumentCaptor<List<Post>>()
        // empty list is a workaround for null capture return
        Mockito.verify(dao).insertPosts(inserted.capture() ?: emptyList())

        MatcherAssert.assertThat(inserted.value.size, CoreMatchers.`is`(1))
        val first = inserted.value[0]
        MatcherAssert.assertThat(first.id, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(first.userId, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(first.title, CoreMatchers.`is`("title"))
        MatcherAssert.assertThat(first.body, CoreMatchers.`is`("body"))

        updatedDbData.value = inserted.value[0]
        Mockito.verify(observer).onChanged(Resource.success(inserted.value[0]))
    }

}