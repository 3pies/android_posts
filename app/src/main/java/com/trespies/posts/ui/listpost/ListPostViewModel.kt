package com.trespies.posts.ui.listpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.trespies.posts.model.Post
import com.trespies.posts.repository.ListPostRepository
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.vo.Resource
import javax.inject.Inject

@OpenForTesting
class ListPostViewModel @Inject constructor(postRepository: ListPostRepository) : ViewModel() {

    val results: LiveData<Resource<List<Post>>> = MutableLiveData()

    fun refresh() {

    }

}