package com.trespies.posts.ui.detailpost

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.trespies.posts.AppExecutors
import com.trespies.posts.R
import com.trespies.posts.databinding.CommentItemBinding
import com.trespies.posts.model.Comment
import com.trespies.posts.ui.common.DataBoundListAdapter

/**
 * A RecyclerView adapter for [Comment] class.
 */
class CommentListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val clickCallback: ((Comment) -> Unit)?
) : DataBoundListAdapter<Comment, CommentItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.email == newItem.email
                    && oldItem.body == newItem.body
        }
    }
) {

    override fun createBinding(parent: ViewGroup): CommentItemBinding {
        val binding = DataBindingUtil.inflate<CommentItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.comment_item,
            parent,
            false,
            dataBindingComponent
        )
        binding.root.setOnClickListener {
            binding.comment?.let {
                clickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: CommentItemBinding, item: Comment) {
        binding.comment = item
    }
}