package com.trespies.posts.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.trespies.posts.TestApp

/**
 * Custom runner to disable dependency injection.
 */
class PostTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, TestApp::class.java.name, context)
    }
}