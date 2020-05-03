package com.trespies.posts.ui.common

import androidx.lifecycle.LiveData
import com.trespies.posts.util.AbsentLiveData

data class ObjectId<V>(val obj: V? = null, var version: Int = 0) {
    fun <T> ifExists(f: (V?) -> LiveData<T>): LiveData<T> {
        return if (obj == null) {
            AbsentLiveData.create()
        } else {
            f(obj)
        }
    }

    fun upgrade() {
        this.version++
    }

    fun reset() {
        this.version = 0
    }

}