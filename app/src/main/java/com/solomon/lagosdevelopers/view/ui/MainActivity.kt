package com.solomon.lagosdevelopers.view.ui

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.ActivityMainBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.utils.NetworkUtils
import com.solomon.lagosdevelopers.utils.Resource
import com.solomon.lagosdevelopers.view.adapter.DevelopersAdapter
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: DevelopersViewModel

    //val viewModel: DevelopersViewModel by viewModels()
//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory

//    val viewModel by lazy {
//        ViewModelProvider(this, viewModelFactory)[DevelopersViewModel::class.java]
//    }

    private lateinit var developersAdapter: DevelopersAdapter
    private var developersItem: MutableList<DevelopersItem> = ArrayList()
    private var position: Int = 0

    private val mProgressDialog by lazy {
        val myDialog = ProgressDialog(this)
        myDialog.setMessage("Fetching...")
        myDialog.setTitle("Lagos Devs")
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
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        developersAdapter = DevelopersAdapter()

        binding.apply {
            developersRecyclerList.apply {
                adapter = developersAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }

//            viewModel.movieResponse.observe(this@MainActivity) {
//                when (it) {
//                    is Resource.Loading<*> -> {
//                        progressBar.isVisible = true
//                    }
//                    is Resource.Error<*> -> {
//                        Snackbar.make(
//                            developersListLayout,
//                            "Data fetching failed",
//                            Snackbar.LENGTH_LONG
//                        ).show()
//                        progressBar.isVisible = false
//                    }
//                    is Resource.Success<*> -> {
//                        developersAdapter.differ.submitList(developersItem)
//                        progressBar.isVisible = false
//                    }
//                }
//            }
        }

        fetchDevelopersList()

        viewModel.errorWatcher.observe(this, Observer {
            it?.printStackTrace()
            mProgressDialog.dismiss()
            showError(it)
        })
    }

    private fun fetchDevelopersList() {

        mProgressDialog.show()

//            viewModel.getAllDevelopers.observe(this@MainActivity) { result ->
//                developersAdapter.differ.submitList(result.data)
//
//                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()
//            }

        viewModel.getAllCardTransactions().observe(this) { result ->
            mProgressDialog.dismiss()
            when {
                !result?.items.isNullOrEmpty() -> {
                    developersItem.clear()
                    result?.items?.let {
                        developersItem.addAll(it)
                        developersAdapter.differ.submitList(developersItem)
                        Timber.e(Gson().toJson(result))
                    }
                }
                result?.items?.isEmpty() == true -> {
                    mProgressDialog.dismiss()
                    val response = result
                    val snackBar: Snackbar = Snackbar.make(
                        developersListLayout,
                        "Data not currently available. \n" +
                                "Please, try again.", Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
                    snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.red))
                    snackBar.setAction("Okay") {
                        snackBar.dismiss()
                    }
                    snackBar.show()
                }
            }

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
                        filter(s.toString(), result?.items ?: emptyList())
                    } catch (e: Throwable) {
                    }
                }
            })
        }
    }

    fun filter(text: String?, data: List<DevelopersItem>) {
        if (::developersAdapter.isInitialized) {
            val temp: MutableList<DevelopersItem> = ArrayList()
            for (items in data) {
                if (items.id.toString().lowercase(Locale.getDefault()).contains(text.toString()) ||
                    items.id.toString().uppercase(Locale.getDefault()).contains(text.toString())
                ) {
                    temp.add(items)
                }
            }
            //update recyclerview
            developersAdapter.differ.submitList(temp)
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