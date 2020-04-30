/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.trespies.podcast.binding


import android.graphics.drawable.Drawable

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.trespies.posts.testing.OpenForTesting
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.trespies.posts.util.GlideApp
import timber.log.Timber

import javax.inject.Inject

/**
 * Binding adapters that work with a fragment instance.
 */
@OpenForTesting
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        url?.let {

            val drawable = imageView.drawable

            GlideApp.with(fragment)
                    .load(it)
                    .listener(getRequestListener())
                    .error(drawable)
                    .placeholder(drawable)
                    .into(imageView)
                    .waitForLayout()
        }
    }

    @BindingAdapter("imageUrlRounded")
    fun bindImageRounded(imageView: ImageView, url: String?) {
        url?.let {
            val drawable = imageView.drawable

            GlideApp.with(fragment)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .listener(getRequestListener())
                    .error(drawable)
                    .placeholder(drawable)
                    .into(imageView)
                    .waitForLayout()
        }
    }

    /**
     * Captura de errores e informe mediante log
     *
     * @return RequestListener
     */
    private fun getRequestListener(): RequestListener<Drawable> {
        return object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                Timber.w(e)
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                return false
            }
        }
    }

}
