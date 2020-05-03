package com.trespies.posts.ui.listpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.trespies.posts.binding.FragmentDataBindingComponent
import com.trespies.posts.util.autoCleared
import com.trespies.posts.AppExecutors
import com.trespies.posts.R
import com.trespies.posts.databinding.PostsFragmentBinding
import com.trespies.posts.di.Injectable
import com.trespies.posts.ui.common.RetryCallback
import timber.log.Timber
import javax.inject.Inject

class ListPostFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<PostsFragmentBinding>()
    var adapter by autoCleared<PostListAdapter>()
    val viewModel: ListPostViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.posts_fragment,
            container,
            false,
            dataBindingComponent
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        val rvAdapter = PostListAdapter(
            dataBindingComponent = dataBindingComponent,
            appExecutors = appExecutors
        ) { post ->
            Timber.d(post.toString())
        }
        binding.list.adapter = rvAdapter
        adapter = rvAdapter

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.refresh()
            }
        }

        viewModel.refresh()
    }

    private fun initRecyclerView() {
        binding.results = viewModel.results
        viewModel.results.observe(viewLifecycleOwner, Observer { result ->
            adapter.submitList(result.data)
        })
    }

}