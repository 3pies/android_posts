package com.trespies.posts.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trespies.posts.util.TestUtil
import com.trespies.posts.util.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepoDaoTest : DbTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun insertAndReadPost() {
        val post = TestUtil.createPost(1, 2, "title", "body")
        db.postsDAO().insertPosts(listOf(post))

        val loaded = db.postsDAO().loadPosts().getOrAwaitValue()

        assertThat(loaded, notNullValue())
        assertThat(loaded.size, `is`(1))
        val item = loaded[0]
        assertThat(item.id, `is`(1))
        assertThat(item.userId, `is`(2))
        assertThat(item.title, `is`("title"))
        assertThat(item.body, `is`("body"))
    }

    @Test
    fun insertPostThenUpdatePost() {
        val post = TestUtil.createPost(1, 2, "title", "body")
        db.postsDAO().insertPosts(listOf(post))

        var data = db.postsDAO().loadPosts()

       assertThat(data.getOrAwaitValue().size, `is`(1))
        val update = TestUtil.createPost(1, 3, "title", null)
        db.postsDAO().insertPosts(listOf(update))
        data = db.postsDAO().loadPosts()

       assertThat(data.getOrAwaitValue().size, `is`(1))
    }

    @Test
    fun insertPostThenDeletePost() {
        val post = TestUtil.createPost(1, 2, "title", "body")
        db.postsDAO().insertPosts(listOf(post))

        var data = db.postsDAO().loadPosts()

        assertThat(data.getOrAwaitValue().size, `is`(1))

        db.postsDAO().deletePosts()
        data = db.postsDAO().loadPosts()

        assertThat(data.getOrAwaitValue().size, `is`(0))
    }

    @Test
    fun insertAndReadUser() {
        val user = TestUtil.createUser(1, "name", "email.info")
        db.postsDAO().insertUser(user)

        val loaded = db.postsDAO().loadUser(1).getOrAwaitValue()

        assertThat(loaded, notNullValue())

        assertThat(loaded.id, `is`(1))
        assertThat(loaded.name, `is`("name"))
        assertThat(loaded.email, `is`("email.info"))
        assertThat(loaded.username, `is`("name"))
    }

    @Test
    fun insertUserThenUpdateUser() {
        val user = TestUtil.createUser(1, "foo", "email.info")
        db.postsDAO().insertUser(user)

        var loaded = db.postsDAO().loadUser(1).getOrAwaitValue()

        assertThat(loaded.name, `is`("foo"))
        val update = TestUtil.createUser(1,  "bar", "email.com")
        db.postsDAO().insertUser(update)
        loaded = db.postsDAO().loadUser(1).getOrAwaitValue()

        assertThat(loaded.name, `is`("bar"))
    }

}