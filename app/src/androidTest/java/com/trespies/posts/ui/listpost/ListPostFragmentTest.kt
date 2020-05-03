package com.trespies.posts.ui.listpost

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trespies.posts.binding.FragmentBindingAdapters
import com.trespies.posts.R
import com.trespies.posts.model.Post
//import com.trespies.posts.binding.FragmentBindingAdapters
import com.trespies.posts.util.CountingAppExecutorsRule
import com.trespies.posts.util.DataBindingIdlingResourceRule
import com.trespies.posts.util.RecyclerViewMatcher
import com.trespies.posts.util.TaskExecutorWithIdlingResourceRule
import com.trespies.posts.util.TestUtil
import com.trespies.posts.util.ViewModelUtil
import com.trespies.posts.util.disableProgressBarAnimations
import com.trespies.posts.util.mock
import com.trespies.posts.vo.Resource
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class ListPostFragmentTest {
    @Rule
    @JvmField
    val executorRule = TaskExecutorWithIdlingResourceRule()
    @Rule
    @JvmField
    val countingAppExecutors = CountingAppExecutorsRule()
    @Rule
    @JvmField
    val dataBindingIdlingResourceRule = DataBindingIdlingResourceRule()

    private val navController = mock<NavController>()

    private val postsLiveData = MutableLiveData<Resource<List<Post>>>()
    private lateinit var viewModel: ListPostViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters

    @Before
    fun init() {
        viewModel = mock<ListPostViewModel>()
        mockBindingAdapter = mock<FragmentBindingAdapters>()
        doNothing().`when`(viewModel).refresh()
        `when`(viewModel.results).thenReturn(postsLiveData)

        val scenario = launchFragmentInContainer(
            themeResId = R.style.AppTheme) {
            val viewmodelFactory = ViewModelUtil.createFor(viewModel)
            ListPostFragment().apply {
                viewModelFactory = viewmodelFactory
                appExecutors = countingAppExecutors.appExecutors
                dataBindingComponent = object : DataBindingComponent {
                    override fun getFragmentBindingAdapters(): FragmentBindingAdapters {
                        return mockBindingAdapter
                    }
                }
            }
        }

        dataBindingIdlingResourceRule.monitorFragment(scenario)
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
            fragment.disableProgressBarAnimations()
        }
    }

    @Test
    fun testLoading() {
        postsLiveData.postValue(Resource.loading(null))
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testPosts() {
        setPosts()
        onView(listMatcher().atPosition(0))
            .check(matches(hasDescendant(withText("title1"))))
        onView(listMatcher().atPosition(1))
            .check(matches(hasDescendant(withText("title2"))))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.list)
    }

    private fun setPosts(vararg names: String) {
        val post1 = TestUtil.createPost(1, 2, "title1", "body1")
        val post2 = TestUtil.createPost(2, 2, "title2", "body2")
        postsLiveData.postValue(Resource.success(listOf(post1, post2)))
    }

}