package com.trespies.posts.api.objects

import java.io.Serializable

data class ApiObjectComment (val id: Int, val postId: Int,
                             val name: String?, val email: String?, val body: String?) : Serializable