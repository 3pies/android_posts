package com.trespies.posts.ui.listpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.trespies.posts.model.Post
import com.trespies.posts.repository.ListPostRepository
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.ui.common.ObjectId
import com.trespies.posts.vo.Resource
import javax.inject.Inject

@OpenForTesting
class ListPostViewModel @Inject constructor(postRepository: ListPostRepository) : ViewModel() {

    private val search = MutableLiveData<ObjectId<Int>>()

    val results: LiveData<Resource<List<Post>>> = Transformations
        .switchMap(search) { search ->
            search.ifExists { postRepository.loadPosts() }
        }

    fun refresh() {
        if (search.value == null) {
            search.value = ObjectId(1)
        } else {
            val value = search.value
            value?.upgrade()
            search.value = value
        }
    }

}