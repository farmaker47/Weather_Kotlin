/*
 * Copyright 2021, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.george.news.bindingAdapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.george.news.R
import com.george.news.network.Articles
import com.george.news.news_fragment.NewsRecyclerViewAdapter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/**
 *  Use Glide to load the image to the ImageView
 */
@BindingAdapter("articleImage")
fun bindImageView(imageView: ImageView, string: String?) {
    Glide.with(imageView.context).load(string).placeholder(R.drawable.ic_broken_image).centerCrop()
        .into(imageView)
}

/**
 * Convert publishedAt time to milliseconds and then to hour
 */
@BindingAdapter("convertToHour")
fun bindTextView(textView: TextView, string: String?) {
    try {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        format.timeZone = TimeZone.getTimeZone("UTC")
        val date = format.parse(string)
        val millis = date.time
        val d = Date(System.currentTimeMillis() - millis)
        val sdf = SimpleDateFormat("HH", Locale.getDefault())
        val dateStr: String = sdf.format(d)

        if (dateStr.take(1) == "0") {
            textView.text = dateStr.substring(1) + textView.context.getString(R.string.hour_char)
        } else {
            textView.text = dateStr + textView.context.getString(R.string.hour_char)
        }
    } catch (e: Exception) {
        Log.e("ERROR_TIME", e.toString())
    }


}