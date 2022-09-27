package com.solomon.lagosdevelopers.view.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.AdapterDevelopersListItemBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.view.ui.DeveloperDetailsActivity

class DevelopersAdapter(var developersDetails: MutableList<DevelopersItem>) :
    RecyclerView.Adapter<DevelopersAdapter.MyViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<DevelopersItem>) {
        developersDetails = list
        notifyDataSetChanged()
    }

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
        val currentItem = differ.currentList[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class MyViewHolder(private val binding: AdapterDevelopersListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var starred = false

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(developersDetails: DevelopersItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(developersDetails.avatar_url)
                    .into(developerImg)

                developerId.text = developersDetails.id.toString()
                developerUrl.text = developersDetails.url
                developerType.text = developersDetails.type
                containerList.setOnClickListener {
                    val intent = Intent(itemView.context, DeveloperDetailsActivity::class.java)
                    intent.putExtra(DeveloperDetailsActivity.DEVELOPER_DETAILS, developersDetails)
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

    private val differCallback = object : DiffUtil.ItemCallback<DevelopersItem>() {
        override fun areItemsTheSame(oldItem: DevelopersItem, newItem: DevelopersItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: DevelopersItem, newItem: DevelopersItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)
}

class DevelopersComparator : DiffUtil.ItemCallback<DevelopersItem>() {
    override fun areItemsTheSame(oldItem: DevelopersItem, newItem: DevelopersItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: DevelopersItem, newItem: DevelopersItem) =
        oldItem == newItem
}