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

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
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
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.loading_animation)
            //Glide.with(statusImageView.context).load(R.drawable.loading_animation).into(statusImageView)
        }
        WeatherApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        WeatherApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("weatherIconDisplay")
fun bindWeatherIcon(iconImageView: ImageView, integerValue: Int?) {
    when (integerValue) {
        in 200..232 -> iconImageView.setImageResource(R.drawable.art_storm)
        in 300..321 -> iconImageView.setImageResource(R.drawable.art_light_rain)
        in 500..504 -> iconImageView.setImageResource(R.drawable.art_rain)
        511 -> iconImageView.setImageResource(R.drawable.art_snow)
        in 520..531 -> iconImageView.setImageResource(R.drawable.art_rain)
        in 600..622 -> iconImageView.setImageResource(R.drawable.art_snow)
        in 701..761 -> iconImageView.setImageResource(R.drawable.art_fog)
        761 -> iconImageView.setImageResource(R.drawable.art_storm)
        771 -> iconImageView.setImageResource(R.drawable.art_storm)
        781 -> iconImageView.setImageResource(R.drawable.art_storm)
        800 -> iconImageView.setImageResource(R.drawable.art_clear)
        801 -> iconImageView.setImageResource(R.drawable.art_light_clouds)
        in 802..804 -> iconImageView.setImageResource(R.drawable.art_clouds)
        in 900..906 -> iconImageView.setImageResource(R.drawable.art_storm)
        in 958..962 -> iconImageView.setImageResource(R.drawable.art_storm)
        in 951..957 -> iconImageView.setImageResource(R.drawable.art_clear)
        else -> iconImageView.setImageResource(R.drawable.art_clear)
    }

    /*if (weatherId >= 200 && weatherId <= 232) {
        return R.drawable.art_storm;
    } else if (weatherId >= 300 && weatherId <= 321) {
        return R.drawable.art_light_rain;
    } else if (weatherId >= 500 && weatherId <= 504) {
        return R.drawable.art_rain;
    } else if (weatherId == 511) {
        return R.drawable.art_snow;
    } else if (weatherId >= 520 && weatherId <= 531) {
        return R.drawable.art_rain;
    } else if (weatherId >= 600 && weatherId <= 622) {
        return R.drawable.art_snow;
    } else if (weatherId >= 701 && weatherId <= 761) {
        return R.drawable.art_fog;
    } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
        return R.drawable.art_storm;
    } else if (weatherId == 800) {
        return R.drawable.art_clear;
    } else if (weatherId == 801) {
        return R.drawable.art_light_clouds;
    } else if (weatherId >= 802 && weatherId <= 804) {
        return R.drawable.art_clouds;
    } else if (weatherId >= 900 && weatherId <= 906) {
        return R.drawable.art_storm;
    } else if (weatherId >= 958 && weatherId <= 962) {
        return R.drawable.art_storm;
    } else if (weatherId >= 951 && weatherId <= 957) {
        return R.drawable.art_clear;
    }*/
}

@BindingAdapter("subStringValue")
fun bindSubString(dateTextView: TextView, string: String?) {
    val substr: String = string!!.substring(5)
    dateTextView.text = substr
}
