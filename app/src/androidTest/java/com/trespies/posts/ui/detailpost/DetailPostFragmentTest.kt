package com.trespies.posts.ui.detailpost

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.trespies.posts.R
import com.trespies.posts.binding.FragmentBindingAdapters
import com.trespies.posts.model.Comment
import com.trespies.posts.model.Post
import com.trespies.posts.model.User
import com.trespies.posts.util.*
import com.trespies.posts.vo.Resource
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class DetailPostFragmentTest {

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

    private val postLiveData = MutableLiveData<Resource<Post>>()
    private val userLiveData = MutableLiveData<Resource<User>>()
    private val commentsLiveData = MutableLiveData<Resource<List<Comment>>>()
    private lateinit var viewModel: DetailPostViewModel
    private lateinit var mockBindingAdapter: FragmentBindingAdapters

    @Before
    fun init() {
        viewModel = mock<DetailPostViewModel>()
        mockBindingAdapter = mock<FragmentBindingAdapters>()
        Mockito.doNothing().`when`(viewModel).setId(ArgumentMatchers.anyInt())
        `when`(viewModel.post).thenReturn(postLiveData)
        `when`(viewModel.user).thenReturn(userLiveData)
        `when`(viewModel.comments).thenReturn(commentsLiveData)

        val scenario = launchFragmentInContainer(
            DetailPostFragmentArgs(1).toBundle(), themeResId = R.style.AppTheme) {
            val viewmodelFactory = ViewModelUtil.createFor(viewModel)
            DetailPostFragment().apply {
                appExecutors = countingAppExecutors.appExecutors
                viewModelFactory = viewmodelFactory
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
        postLiveData.postValue(Resource.loading(null))
        userLiveData.postValue(Resource.loading(null))
        commentsLiveData.postValue(Resource.loading(null))
        Espresso.onView(ViewMatchers.withId(R.id.load_more_bar))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.retry))
            .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isDisplayed())))
    }

}