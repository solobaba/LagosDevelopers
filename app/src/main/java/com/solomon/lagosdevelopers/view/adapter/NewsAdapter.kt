package com.solomon.lagosdevelopers.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.AdapterDevelopersListItemBinding
import com.solomon.lagosdevelopers.model.response.*
import com.solomon.lagosdevelopers.view.ui.NewsDetailActivity

class DevelopersAdapter :
    ListAdapter<NewsData, DevelopersAdapter.MyViewHolder>(DevelopersComparator()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val binding = AdapterDevelopersListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class MyViewHolder(private val binding: AdapterDevelopersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var starred = false

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(resultDetails: NewsData) {
            binding.apply {
                Glide.with(itemView)
                    .load(resultDetails.urlToImage)
                    .into(developerImg)

                author.text = resultDetails.author.toString()
                title.text = resultDetails.title
                name.text = resultDetails.source.name
                containerList.setOnClickListener {
                    val intent = Intent(itemView.context, NewsDetailActivity::class.java)
                    intent.putExtra(NewsDetailActivity.DEVELOPER_DETAILS, resultDetails)
                    itemView.context.startActivity(intent)
                }

                favIcon.setOnClickListener {
                    if (starred) {
                        val starEmpty: Drawable =
                            itemView.resources.getDrawable(R.drawable.ic_baseline_star_border_24)
                        starEmpty.setBounds(0, 0, 24, 24)
                        favIcon.background = starEmpty
                    } else {
                        val startFilled: Drawable =
                            itemView.resources.getDrawable(R.drawable.ic_baseline_star_rate_24)
                        startFilled.setBounds(0, 0, 24, 24)
                        favIcon.background = startFilled

                        Snackbar.make(itemView, "Marked as favorite profile", Snackbar.LENGTH_LONG)
                            .show()
                    }
                    starred = !starred
                }
            }
        }
    }
}

class DevelopersComparator : DiffUtil.ItemCallback<NewsData>() {
    override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData) =
        oldItem == newItem
}