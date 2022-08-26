package com.solomon.lagosdevelopers.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.view.ui.DeveloperDetailsActivity

class DevelopersAdapter(
    var context: Context,
    var developersDetails: MutableList<DevelopersItem>
) : RecyclerView.Adapter<DevelopersAdapter.MyViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: MutableList<DevelopersItem>) {
        developersDetails = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.adapter_developers_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val devImage: DevelopersItem = developersDetails[position]
        val uri: Uri = Uri.parse(devImage.avatar_url)
        Glide.with(context).load(uri).into(holder.developerImage)
        holder.developerID.text = developersDetails[position].id.toString()
        holder.developerUrl.text = developersDetails[position].url
        holder.developerType.text = developersDetails[position].type

        holder.layout.setOnClickListener {
            val intent = Intent(context, DeveloperDetailsActivity::class.java)
            intent.putExtra(DeveloperDetailsActivity.DEVELOPER_DETAILS, developersDetails[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return developersDetails.size
    }

    class MyViewHolder(var contentView: View) : RecyclerView.ViewHolder(contentView) {
        var layout: ConstraintLayout = contentView.findViewById(R.id.container_list)
        var developerImage: ImageView = contentView.findViewById(R.id.developer_img)
        var developerID: TextView = contentView.findViewById(R.id.developer_id)
        var developerUrl: TextView = contentView.findViewById(R.id.developer_url)
        var developerType: TextView = contentView.findViewById(R.id.developer_type)
    }
}