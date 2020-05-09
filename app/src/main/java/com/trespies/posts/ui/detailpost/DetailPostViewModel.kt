package com.trespies.posts.ui.detailpost

import androidx.lifecycle.*
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User
import com.trespies.posts.repository.ListCommentRepository
import com.trespies.posts.repository.PostRepository
import com.trespies.posts.repository.UserRepository
import com.trespies.posts.testing.OpenForTesting
import com.trespies.posts.ui.common.ObjectId
import com.trespies.posts.util.AbsentLiveData
import com.trespies.posts.vo.Resource
import com.trespies.posts.vo.Status
import java.util.*
import javax.inject.Inject

@OpenForTesting
class DetailPostViewModel @Inject constructor(
    postRepository: PostRepository,
    userRepository: UserRepository,
    commentRepository: ListCommentRepository ) : ViewModel() {

    private val objectID = MutableLiveData<ObjectId<Int>>()
    private val mediator = MediatorLiveData<ObjectId<Post>>()

    val post : LiveData<Resource<Post>> = Transformations.switchMap(objectID) { input ->
        input.ifExists { input.obj?.let { id -> postRepository.loadPost(id) } ?: AbsentLiveData.create() }
    }

    val user : LiveData<Resource<User>> = Transformations.switchMap(mediator) { input ->
        input.ifExists { input.obj?.let { post -> userRepository.loadUser(post.userId) } ?: AbsentLiveData.create() }
    }

    val comments : LiveData<Resource<List<Comment>>> = Transformations.switchMap(mediator) { input ->
        input.ifExists { input.obj?.let { post -> commentRepository.loadComments(post.id) } ?: AbsentLiveData.create() }
    }

    init {
        mediator.addSource(post) { result ->
            when(result.status) {
                Status.SUCCESS, Status.ERROR -> {
                    mediator.value = ObjectId(result.data)
                }
            }
        }
    }

    fun setId(postID: Int?) {
        val id = postID ?: return
        val update = ObjectId(id)
        if (Objects.equals(objectID.value, update)) {
            return
        }
        objectID.value = update
    }

    fun refresh() {
        val actual = objectID.value
        if (actual != null) {
            actual.upgrade()
            objectID.value = actual
        }
    }

}