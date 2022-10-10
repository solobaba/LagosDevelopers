package com.solomon.carbonapp.view.ui

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.solomon.data.db.NewsEntity
import com.solomon.carbonapp.databinding.ActivityMainBinding
import com.solomon.carbonapp.view.adapter.DevelopersAdapter
import com.solomon.carbonapp.viewmodel.NewsViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[NewsViewModel::class.java]
    }

    private lateinit var developersAdapter: DevelopersAdapter

    private val mProgressDialog by lazy {
        val myDialog = ProgressDialog(this)
        myDialog.setMessage("Fetching...")
        myDialog.setTitle("News")
        myDialog.setCancelable(false)
        myDialog.setButton(
            DialogInterface.BUTTON_NEGATIVE,
            "Cancel",
            DialogInterface.OnClickListener { dialog, which ->
                myDialog.dismiss() //dismiss dialog
                finish()
            })
        myDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        developersRecyclerList.layoutManager = LinearLayoutManager(this)
        developersAdapter = DevelopersAdapter()
        developersRecyclerList.adapter = developersAdapter

        fetchNewsInfo()

        viewModel.errorWatcher.observe(this, Observer {
            it?.printStackTrace()
            mProgressDialog.dismiss()
            showError(it)
        })
    }

    private fun fetchNewsInfo() {
        mProgressDialog.show()
        viewModel.getAllNewsInfo()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                mProgressDialog.dismiss()
                viewModel.data.collectLatest {
                    developersAdapter.submitList(it.first)
                    Timber.e(Gson().toJson(it))

                    searchBarEdittext.addTextChangedListener(object : TextWatcher {
                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable) {
                            try {
                                filter(s.toString(), (((it.first ?: emptyList()))))
                            } catch (e: Throwable) {
                            }
                        }
                    })
                }
            }
        }
    }

    fun filter(text: String?, data: List<NewsEntity>) {
        if (::developersAdapter.isInitialized) {
            val temp: MutableList<NewsEntity> = ArrayList()
            for (items in data) {
                if (items.author.toString().lowercase(Locale.getDefault()).contains(text.toString()) ||
                    items.author.toString().uppercase(Locale.getDefault()).contains(text.toString())
                ) {
                    temp.add(items)
                }
            }
            //update recyclerview
            developersAdapter.submitList(temp)
        }
    }

    private fun showError(throwable: Throwable?) {
        throwable?.let {
            val message = if (it.localizedMessage.isNullOrEmpty()) {
                it.cause?.localizedMessage ?: "Error occurred."
            } else {
                it.localizedMessage
            }
            Snackbar.make(developersListLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }
}