package com.trespies.posts.api.objects

import java.io.Serializable

data class ApiObjectPost (val id: Int, val userId: Int, val title: String?, val body: String?) : Serializable