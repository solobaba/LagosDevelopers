package com.solomon.lagosdevelopers.view.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.solomon.lagosdevelopers.databinding.ActivityDeveloperDetailsBinding
import com.solomon.lagosdevelopers.model.response.NewsData
import kotlinx.android.synthetic.main.activity_developer_details.*
import timber.log.Timber

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val DEVELOPER_DETAILS = "resultDetails"
    }

    lateinit var newsData: NewsData
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDeveloperDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(DEVELOPER_DETAILS)) {
            newsData = intent.getSerializableExtra(DEVELOPER_DETAILS) as NewsData
            Timber.e(Gson().toJson(newsData))
        }

        initializeData(newsData)
    }

    private fun initializeData(newsData: NewsData) {
        val devImage: NewsData = newsData
        val uri: Uri = Uri.parse(devImage.urlToImage)
        Glide.with(this).load(uri).into(newsImg)

        descriptionText.text = newsData.description
        contentText.text = newsData.content
        confirmBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(newsData.url)
            startActivity(intent)
            //onBackPressed()
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