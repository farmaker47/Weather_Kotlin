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

package com.george.news.news_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.george.news.R
import com.george.news.network.Articles

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
class NewsRecyclerViewAdapter(
    val onClickListener: OnClickListener,
) :
    PagedListAdapter<Articles, NewsRecyclerViewAdapter.NewsRecyclerViewHolder>(DiffCallback) {
    /**
     * The NewsRecyclerViewHolder constructor takes the binding variable from the associated
     * ForecastListItem, which nicely gives it access to the full [MainInfo] information.
     */
    class NewsRecyclerViewHolder(private var binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mainInfo: Articles) {

            binding.setVariable(BR.mainInfo, mainInfo)
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()

            /*val textView = itemView.findViewById<TextView>(R.id.news_text)
            val imageView = itemView.findViewById<ImageView>(R.id.news_icon)
            textView.text = mainInfo.title
            Glide.with(imageView.context).load(mainInfo.urlToImage).placeholder(R.drawable.ic_broken_image).centerCrop().into(imageView)*/
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [MainInfo]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Articles>() {
        override fun areItemsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Articles, newItem: Articles): Boolean {
            return oldItem.title == newItem.title
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsRecyclerViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return NewsRecyclerViewHolder(binding)

        /*return if(viewType == R.layout.news_list_item_first){
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
            NewsRecyclerViewHolder(binding)
        }else{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
            NewsRecyclerViewHolder(binding)
        }*/

    }


    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: NewsRecyclerViewHolder, position: Int) {
        val mainInfo = getItem(position)
        holder.itemView.setOnClickListener {
            mainInfo?.let { it1 -> onClickListener.onClick(it1) }
        }
        mainInfo?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int = currentList?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.news_list_item_first
        else R.layout.news_list_item
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [MarsProperty]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [MarsProperty]
     */
    class OnClickListener(val clickListener: (mainInfo: Articles) -> Unit) {
        fun onClick(mainInfo: Articles) = clickListener(mainInfo)
    }
}
