package com.trespies.posts.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trespies.posts.api.PostService
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.Post
import com.trespies.posts.util.ApiUtil.successCall
import com.trespies.posts.util.InstantAppExecutors
import com.trespies.posts.util.TestUtil
import com.trespies.posts.util.argumentCaptor
import com.trespies.posts.util.mock
import com.trespies.posts.vo.Resource
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@RunWith(JUnit4::class)
class ListPostRepositoryTest {

    private lateinit var repository: ListPostRepository
    private val dao = mock(PostsDao::class.java)
    private val service = mock(PostService::class.java)
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun init() {
        val db = mock(PostsDB::class.java)
        `when`(db.postsDAO()).thenReturn(dao)
        `when`(db.runInTransaction(ArgumentMatchers.any())).thenCallRealMethod()
        repository = ListPostRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadPosts() {
        val dbData = MutableLiveData<List<Post>>()
        `when`(dao.loadPosts()).thenReturn(dbData)

        val data = repository.loadPosts()
        verify(dao).loadPosts()

        verify(service, never()).getPosts()


        val apiPost = TestUtil.createApiPost(1, 2, "title", "body")
        // network does not send these
        val apiPosts = listOf(apiPost)
        val call = successCall(apiPosts)
        `when`(service.getPosts())
            .thenReturn(call)

        val observer = mock<Observer<Resource<List<Post>>>>()
        data.observeForever(observer)

        verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<List<Post>>()
        `when`(dao.loadPosts()).thenReturn(updatedDbData)
        dbData.value = emptyList()

        verify(service).getPosts()
        val inserted = argumentCaptor<List<Post>>()
        // empty list is a workaround for null capture return
        verify(dao).insertPosts(inserted.capture() ?: emptyList())
        verify(dao).deletePosts()

        assertThat(inserted.value.size, `is`(1))
        val first = inserted.value[0]
        assertThat(first.id, `is`(1))
        assertThat(first.userId, `is`(2))
        assertThat(first.title, `is`("title"))
        assertThat(first.body, `is`("body"))

        updatedDbData.value = inserted.value
        verify(observer).onChanged(Resource.success(inserted.value))
    }

}