package com.trespies.posts.api.objects

import java.io.Serializable

data class ApiObjectUser (val id: Int, val name: String?, val surname: String?,
                          val username: String?, val email: String?, val address: ApiObjectAddress?,
                          val phone: String?, val website: String?, val company: ApiObjectCompany?
                          ) : Serializable

