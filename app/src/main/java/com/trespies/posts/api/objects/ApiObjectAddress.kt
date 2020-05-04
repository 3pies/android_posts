package com.trespies.posts.api.objects

import java.io.Serializable

data class ApiObjectAddress (val street: String?, val suite: String?, val city: String?,
                            val zipcode: String?, val geo: ApiObjectGeo?) :
    Serializable