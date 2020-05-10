package com.trespies.posts.ui.detailpost

import androidx.databinding.DataBindingComponent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
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
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.CoreMatchers.not

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
        onView(withId(R.id.load_more_bar)).check(matches(isDisplayed()))
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())))
    }

    @Test
    fun loadingWithData() {
        val post = TestUtil.createPost(1, 2, "title1", "body1")
        val user = TestUtil.createUser(2, "name", "email")
        postLiveData.postValue(Resource.loading(post))
        userLiveData.postValue(Resource.loading(user))
        onView(withId(R.id.description)).check(matches(withText(post.body)))
        onView(withId(R.id.author_name)).check(matches(withText(user.fullName)))
        onView(withId(R.id.load_more_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun testComments() {
        setComments()

        onView(withId(R.id.total_comments)).check(matches(withText("3")))

        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("name1"))))
        onView(listMatcher().atPosition(0)).check(matches(hasDescendant(withText("email1@gmail.com"))))

        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("name2"))))
        onView(listMatcher().atPosition(1)).check(matches(hasDescendant(withText("email2@mail"))))

        onView(listMatcher().atPosition(2)).check(matches(hasDescendant(withText("name3"))))
        onView(listMatcher().atPosition(2)).check(matches(hasDescendant(withText("email3@mail.co.uk \uD83C\uDDEC\uD83C\uDDE7"))))
    }

    private fun listMatcher(): RecyclerViewMatcher {
        return RecyclerViewMatcher(R.id.list)
    }

    private fun setComments() {
        val comment1 = TestUtil.createComment(1, 1, "name1", "email1@gmail.com", "body1")
        val comment2 = TestUtil.createComment(2, 1, "name2", "email2@mail", "body2")
        val comment3 = TestUtil.createComment(3, 1, "name3", "email3@mail.co.uk", "body3")
        commentsLiveData.postValue(Resource.success(listOf(comment1, comment2, comment3)))
    }

}