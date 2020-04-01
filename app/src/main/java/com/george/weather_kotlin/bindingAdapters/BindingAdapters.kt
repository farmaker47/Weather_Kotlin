/*
 * Copyright 2018, The Android Open Source Project
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

package com.george.weather_kotlin.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.george.weather_kotlin.R
import com.george.weather_kotlin.network.MainInfo
import com.george.weather_kotlin.weather_fragment.WeatherApiStatus
import com.george.weather_kotlin.weather_fragment.WeatherRecyclerViewAdapter

/**
 * When there is no MainInfo data (data is null), hide the [RecyclerView], otherwise show it.
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<MainInfo>?) {
    val adapter = recyclerView.adapter as WeatherRecyclerViewAdapter
    adapter.submitList(data)
}

@BindingAdapter("weatherApiStatus")
fun bindStatus(statusImageView: ImageView, status: WeatherApiStatus?) {
    when (status) {
        WeatherApiStatus.LOADING -> {
            Log.e("LOADING","YES")
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
            //Glide.with(statusImageView.context).load(R.drawable.loading_animation).into(statusImageView)
        }
        WeatherApiStatus.ERROR -> {
            Log.e("ERROR","YES")
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        WeatherApiStatus.DONE -> {
            Log.e("DONE","YES")
            statusImageView.visibility = View.GONE
        }
    }
}
