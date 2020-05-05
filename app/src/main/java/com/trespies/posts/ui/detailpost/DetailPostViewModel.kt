package com.trespies.posts.ui.detailpost

import androidx.lifecycle.ViewModel
import com.trespies.posts.repository.ListCommentRepository
import com.trespies.posts.repository.PostRepository
import com.trespies.posts.repository.UserRepository
import com.trespies.posts.testing.OpenForTesting
import javax.inject.Inject

@OpenForTesting
class DetailPostViewModel @Inject constructor(
    postRepository: PostRepository,
    userRepository: UserRepository,
    commentRepository: ListCommentRepository ) : ViewModel() {

}