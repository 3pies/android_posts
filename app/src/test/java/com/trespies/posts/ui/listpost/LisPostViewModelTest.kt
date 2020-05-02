package com.trespies.posts.ui.listpost

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.trespies.posts.model.Post
import com.trespies.posts.repository.ListPostRepository
import com.trespies.posts.util.mock
import com.trespies.posts.vo.Resource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@RunWith(JUnit4::class)
class LisPostViewModelTest {
    @Rule
    @JvmField
    val instantExecutor = InstantTaskExecutorRule()
    private val repository = Mockito.mock(ListPostRepository::class.java)
    private lateinit var viewModel: ListPostViewModel

    @Before
    fun init() {
        // need ot init after instant executor rule is established.
        viewModel = ListPostViewModel(repository)
    }

    @Test
    fun empty() {
        val result = mock<Observer<Resource<List<Post>>>>()
        viewModel.results.observeForever(result)
        viewModel.refresh()
        Mockito.verifyNoMoreInteractions(repository)
    }

    @Test
    fun basic() {
        val result = mock<Observer<Resource<List<Post>>>>()
        viewModel.results.observeForever(result)
        viewModel.refresh()
        Mockito.verify(repository).loadPosts()
    }

    @Test
    fun refresh() {
        viewModel.refresh()
        Mockito.verifyNoMoreInteractions(repository)
        viewModel.refresh()
        Mockito.verifyNoMoreInteractions(repository)

        viewModel.results.observeForever(mock())
        Mockito.verify(repository).loadPosts()
        Mockito.reset(repository)
        viewModel.refresh()
        Mockito.verify(repository).loadPosts()
    }
}