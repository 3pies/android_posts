package com.trespies.posts.api.clients

import com.trespies.posts.api.PostService
import com.trespies.posts.services.Configuration

class PostsRestClient(private val configuration: Configuration) : AbstractRestClient<PostService>() {
    override val urlBase: String
        get() = configuration.urlApiWS
    override val iClassService: Class<PostService>
        get() = PostService::class.java

}