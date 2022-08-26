package com.solomon.lagosdevelopers.view.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.ActivityDeveloperDetailsBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
import com.solomon.lagosdevelopers.viewmodel.ViewModelFactoryForAny
import timber.log.Timber

class DeveloperDetailsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactoryForAny("Repository")
        )[DevelopersViewModel::class.java]
    }

    companion object {
        const val DEVELOPER_DETAILS = "developersDetails"
    }

    lateinit var binding: ActivityDeveloperDetailsBinding
    lateinit var developersItem: DevelopersItem
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_developer_details)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

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
        Glide.with(this).load(uri).into(binding.developerImg)

        binding.devLogin.text = developersItem.login
        binding.devId.text = developersItem.id.toString()
        binding.devUrl.text = developersItem.url
        binding.devScore.text = developersItem.score.toString()
        binding.devType.text = developersItem.type
        binding.confirmBtn.setOnClickListener{
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