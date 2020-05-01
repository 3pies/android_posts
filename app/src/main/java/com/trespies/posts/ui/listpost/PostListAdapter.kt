package com.trespies.posts.ui.listpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.trespies.posts.AppExecutors
import com.trespies.posts.R
import com.trespies.posts.databinding.PostItemBinding
import com.trespies.posts.model.Post
import com.trespies.posts.ui.common.DataBoundListAdapter

/**
 * A RecyclerView adapter for [Post] class.
 */
class PostListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Post) -> Unit)?
) : DataBoundListAdapter<Post, PostItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title
                    && oldItem.body == newItem.body
        }
    }
) {

    override fun createBinding(parent: ViewGroup): PostItemBinding {
        val binding = DataBindingUtil.inflate<PostItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.post_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.post?.let {
                clickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PostItemBinding, item: Post) {
        binding.post = item
    }
}