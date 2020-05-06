package com.trespies.posts.ui.detailpost

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User
import com.trespies.posts.repository.ListCommentRepository
import com.trespies.posts.repository.PostRepository
import com.trespies.posts.repository.UserRepository
import com.trespies.posts.util.mock
import com.trespies.posts.vo.Resource
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.Mockito.*

@RunWith(JUnit4::class)
class DetailPostViewModelTest {

    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val postRepository = Mockito.mock(PostRepository::class.java)
    private val userRepository = Mockito.mock(UserRepository::class.java)
    private val commentRepository = Mockito.mock(ListCommentRepository::class.java)

    private lateinit var viewModel: DetailPostViewModel

    @Before
    fun init() {
        viewModel = DetailPostViewModel(postRepository, userRepository, commentRepository)
    }

    @Test
    fun testNull() {
        assertThat(viewModel.post, notNullValue())
        verify(postRepository, never()).loadPost(anyInt())
        viewModel.setId(1)
        verify(postRepository, never()).loadPost(anyInt())
    }

    @Test
    fun basic() {
        val resultPost = mock<Observer<Resource<Post>>>()
        viewModel.post.observeForever(resultPost)
        val resultUser = mock<Observer<Resource<User>>>()
        viewModel.user.observeForever(resultUser)
        val resultComents = mock<Observer<Resource<List<Comment>>>>()
        viewModel.comments.observeForever(resultComents)

        val returnValue = MutableLiveData<Resource<Post>>()
        returnValue.value = Resource.success(Post(1, 2, "title", "body"))
        `when`(postRepository.loadPost(1)).thenReturn(returnValue)

        viewModel.setId(1)

        verify(postRepository).loadPost(1)
        verify(userRepository).loadUser(2)
        verify(commentRepository).loadComments(1)

    }

    @Test
    fun refresh() {
        val result = mock<Observer<Resource<Post>>>()
        viewModel.post.observeForever(result)

        viewModel.setId(1)
        viewModel.refresh()

        verify(postRepository, times(2)).loadPost(1)
    }

    @Test
    fun resetSameQuery() {
        val result = mock<Observer<Resource<Post>>>()
        viewModel.post.observeForever(result)

        viewModel.setId(1)
        viewModel.setId(1)

        verify(postRepository, times(1)).loadPost(1)
    }

}