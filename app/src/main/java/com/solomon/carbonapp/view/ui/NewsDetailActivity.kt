package com.solomon.carbonapp.view.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.solomon.data.db.NewsEntity
import com.solomon.carbonapp.databinding.ActivityDeveloperDetailsBinding
import kotlinx.android.synthetic.main.activity_developer_details.*
import timber.log.Timber

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val DEVELOPER_DETAILS = "resultDetails"
    }

    private var newsData: NewsEntity? = null
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDeveloperDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(DEVELOPER_DETAILS)) {
            newsData = intent.getParcelableExtra(DEVELOPER_DETAILS) as? NewsEntity
            Timber.e(Gson().toJson(newsData))
        }

        newsData?.let { initializeData(it) }
    }

    private fun initializeData(newsData: NewsEntity) {
        val devImage: NewsEntity = newsData
        val uri: Uri = Uri.parse(devImage.imageUrl)
        Glide.with(this).load(uri).into(newsImg)

        descriptionText.text = newsData.description
        contentText.text = newsData.content
        confirmBtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(newsData.fullArticleUrl)
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