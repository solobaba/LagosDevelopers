package com.solomon.lagosdevelopers.view.ui

import android.app.ProgressDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.ActivityMainBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.utils.NetworkUtils
import com.solomon.lagosdevelopers.view.adapter.DevelopersAdapter
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
import com.solomon.lagosdevelopers.viewmodel.ViewModelFactoryForAny
import timber.log.Timber
import java.util.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactoryForAny("Repository")
        )[DevelopersViewModel::class.java]
    }

    lateinit var binding: ActivityMainBinding

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
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.developersRecyclerList.layoutManager = LinearLayoutManager(this)
        developersAdapter = DevelopersAdapter(this, developersItem)
        binding.developersRecyclerList.adapter = developersAdapter

        fetchDevelopersList()

        viewModel.errorWatcher.observe(this, Observer {
            it?.printStackTrace()
            mProgressDialog.dismiss()
            showError(it)
        })
    }

    private fun fetchDevelopersList() {
        if (!NetworkUtils.isConnectionAvailable(this)) {
            val snackBar: Snackbar = Snackbar.make(
                binding.developersListLayout,
                "No Internet Connection. Please, turn on your " +
                        "\ninternet connection and press the Okay button",
                Snackbar.LENGTH_INDEFINITE
            )
            snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.white))
            snackBar.setBackgroundTint(ContextCompat.getColor(this, R.color.red))
            snackBar.setAction("Okay") {
                fetchDevelopersList()
                snackBar.dismiss()
            }
            snackBar.show()
        } else {
            mProgressDialog.show()
            viewModel.getAllCardTransactions().observe(this, Observer { result ->
                mProgressDialog.dismiss()
                when {
                    !result?.items.isNullOrEmpty() -> {
                        developersItem.clear()
                        result?.items?.let {
                            developersItem.addAll(it)
                            developersAdapter.updateList(developersItem)
                            Timber.e(result.toString())
                        }
                    }
                    result?.items?.isEmpty() == true -> {
                        mProgressDialog.dismiss()
                        val response = result
                        val snackBar: Snackbar = Snackbar.make(
                            binding.developersListLayout,
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

                binding.searchBarEdittext.addTextChangedListener(object : TextWatcher {
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
            developersAdapter.updateList(temp)
        }
    }

    private fun showError(throwable: Throwable?) {
        throwable?.let {
            val message = if (it.localizedMessage.isNullOrEmpty()) {
                it.cause?.localizedMessage ?: "Error occurred."
            } else {
                it.localizedMessage
            }
            Snackbar.make(binding.developersListLayout, message, Snackbar.LENGTH_LONG).show()
        }
    }
}