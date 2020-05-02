package com.trespies.posts

import android.app.Application

/**
 * We use a separate App for tests to prevent initializing dependency injection.
 *
 * See [com.trespies.posts.util.PostTestRunner].
 */
class TestApp : Application()