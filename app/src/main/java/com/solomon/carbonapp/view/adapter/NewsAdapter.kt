package com.solomon.carbonapp.view.adapter

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
import com.solomon.data.db.NewsEntity
import com.solomon.carbonapp.R
import com.solomon.carbonapp.databinding.AdapterDevelopersListItemBinding
import com.solomon.carbonapp.view.ui.NewsDetailActivity

class DevelopersAdapter :
    ListAdapter<NewsEntity, DevelopersAdapter.MyViewHolder>(DevelopersComparator()) {

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
        fun bind(resultDetails: NewsEntity) {
            binding.apply {
                Glide.with(itemView)
                    .load(resultDetails.imageUrl)
                    .into(developerImg)

                author.text = resultDetails.author.toString()
                title.text = resultDetails.title
                name.text = resultDetails.name
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

class DevelopersComparator : DiffUtil.ItemCallback<NewsEntity>() {
    override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity) =
        oldItem == newItem
}