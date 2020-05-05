package com.trespies.posts.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trespies.posts.api.PostService
import com.trespies.posts.db.PostsDB
import com.trespies.posts.db.PostsDao
import com.trespies.posts.model.Comment
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
class ListCommentRepositoryTest {

    private lateinit var repository: ListCommentRepository
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
        repository = ListCommentRepository(InstantAppExecutors(), dao, service)
    }

    @Test
    fun loadPosts() {
        val dbData = MutableLiveData<List<Comment>>()
        Mockito.`when`(dao.loadCommentsByPostID(1)).thenReturn(dbData)

        val data = repository.loadComments(1)
        Mockito.verify(dao).loadCommentsByPostID(1)

        Mockito.verify(service, Mockito.never()).getCommentsByPostID(1)


        val apiComment = TestUtil.createApiComment(2, 1, "name", "email@info.co", "body")
        // network does not send these
        val apiComments = listOf(apiComment)
        val call = ApiUtil.successCall(apiComments)
        Mockito.`when`(service.getCommentsByPostID(1))
            .thenReturn(call)

        val observer = mock<Observer<Resource<List<Comment>>>>()
        data.observeForever(observer)

        Mockito.verify(observer).onChanged(Resource.loading(null))

        val updatedDbData = MutableLiveData<List<Comment>>()
        Mockito.`when`(dao.loadCommentsByPostID(1)).thenReturn(updatedDbData)
        dbData.value = emptyList()

        Mockito.verify(service).getCommentsByPostID(1)
        val inserted = argumentCaptor<List<Comment>>()
        // empty list is a workaround for null capture return
        Mockito.verify(dao).insertComments(inserted.capture() ?: emptyList())
        Mockito.verify(dao).deleteCommentsByPostID(1)

        MatcherAssert.assertThat(inserted.value.size, CoreMatchers.`is`(1))
        val first = inserted.value[0]
        MatcherAssert.assertThat(first.id, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(first.postId, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(first.name, CoreMatchers.`is`("name"))
        MatcherAssert.assertThat(first.email, CoreMatchers.`is`("email@info.co"))
        MatcherAssert.assertThat(first.body, CoreMatchers.`is`("body"))

        updatedDbData.value = inserted.value
        Mockito.verify(observer).onChanged(Resource.success(inserted.value))
    }

}