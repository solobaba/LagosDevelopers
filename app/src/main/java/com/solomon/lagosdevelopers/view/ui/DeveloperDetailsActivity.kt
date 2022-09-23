package com.solomon.lagosdevelopers.view.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.solomon.lagosdevelopers.databinding.ActivityDeveloperDetailsBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import kotlinx.android.synthetic.main.activity_developer_details.*
import timber.log.Timber

class DeveloperDetailsActivity : AppCompatActivity() {

    companion object {
        const val DEVELOPER_DETAILS = "developersDetails"
    }

    lateinit var developersItem: DevelopersItem
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDeveloperDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(DEVELOPER_DETAILS)) {
            developersItem = intent.getSerializableExtra(DEVELOPER_DETAILS) as DevelopersItem
            Timber.e(Gson().toJson(developersItem))
        } else {
            finish()
            return
        }

        initializeData(developersItem)
    }

    private fun initializeData(developersItem: DevelopersItem) {
        val devImage: DevelopersItem = developersItem
        val uri: Uri = Uri.parse(devImage.avatar_url)
        Glide.with(this).load(uri).into(developerImg)

        devLogin.text = developersItem.login
        devId.text = developersItem.id.toString()
        devUrl.text = developersItem.url
        devScore.text = developersItem.score.toString()
        devType.text = developersItem.type
        confirmBtn.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return false
    }
}