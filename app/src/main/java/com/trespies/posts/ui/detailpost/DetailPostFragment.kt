package com.trespies.posts.ui.detailpost

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
import androidx.navigation.fragment.navArgs
import com.trespies.posts.AppExecutors
import com.trespies.posts.R
import com.trespies.posts.binding.FragmentDataBindingComponent
import com.trespies.posts.databinding.DetailPostFragmentBinding
import com.trespies.posts.di.Injectable
import com.trespies.posts.ui.common.RetryCallback
import com.trespies.posts.util.autoCleared
import timber.log.Timber
import javax.inject.Inject

class DetailPostFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<DetailPostFragmentBinding>()
    private val params by navArgs<DetailPostFragmentArgs>()
    private var adapter by autoCleared<CommentListAdapter>()

    val viewModel: DetailPostViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.detail_post_fragment,
            container,
            false,
            dataBindingComponent
        )
        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.refresh()
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.d("Post to load %d", params.postID)
        viewModel.setId(params.postID)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.post = viewModel.post
        binding.author = viewModel.user

        val adapter = CommentListAdapter(dataBindingComponent, appExecutors) { comment ->
            Timber.d("Click on comment %s", comment.body)
        }
        this.adapter = adapter
        binding.list.adapter = adapter

        initCommentList()
    }

    private fun initCommentList() {
        viewModel.comments.observe(viewLifecycleOwner, Observer { listResource ->
            if (listResource?.data != null) {
                adapter.submitList(listResource.data)
            } else {
                adapter.submitList(emptyList())
            }
        })
    }

}