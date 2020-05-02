package com.trespies.posts.db

import android.database.sqlite.SQLiteException
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

   /* @Test
    fun insertContributorsWithoutRepo() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        val contributor = TestUtil.createContributor(repo, "c1", 3)
        try {
            db.repoDao().insertContributors(listOf(contributor))
            throw AssertionError("must fail because repo does not exist")
        } catch (ex: SQLiteException) {
        }

    }

    @Test
    fun insertContributors() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        val c1 = TestUtil.createContributor(repo, "c1", 3)
        val c2 = TestUtil.createContributor(repo, "c2", 7)
        db.runInTransaction {
            db.repoDao().insert(repo)
            db.repoDao().insertContributors(arrayListOf(c1, c2))
        }
        val list = db.repoDao().loadContributors("foo", "bar").getOrAwaitValue()
        assertThat(list.size, `is`(2))
        val first = list[0]

        assertThat(first.login, `is`("c2"))
        assertThat(first.contributions, `is`(7))

        val second = list[1]
        assertThat(second.login, `is`("c1"))
        assertThat(second.contributions, `is`(3))
    }

    @Test
    fun createIfNotExists_exists() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        db.repoDao().insert(repo)
        assertThat(db.repoDao().createRepoIfNotExists(repo), `is`(-1L))
    }

    @Test
    fun createIfNotExists_doesNotExist() {
        val repo = TestUtil.createRepo("foo", "bar", "desc")
        assertThat(db.repoDao().createRepoIfNotExists(repo), `is`(1L))
    }
*/
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
}