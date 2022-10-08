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
import com.solomon.lagosdevelopers.R
import com.solomon.lagosdevelopers.databinding.ActivityMainBinding
import com.solomon.lagosdevelopers.model.response.DevelopersItem
import com.solomon.lagosdevelopers.utils.NetworkUtils
import com.solomon.lagosdevelopers.utils.Resource
import com.solomon.lagosdevelopers.view.adapter.DevelopersAdapter
import com.solomon.lagosdevelopers.viewmodel.DevelopersViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[DevelopersViewModel::class.java]
    }

    private lateinit var developersAdapter: DevelopersAdapter
    //private val developersAdapter = DevelopersAdapter()
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
        AndroidInjection.inject(this)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        developersRecyclerList.layoutManager = LinearLayoutManager(this)
        developersAdapter = DevelopersAdapter(developersItem)
        developersRecyclerList.adapter = developersAdapter

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
                developersListLayout,
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
                Toast.makeText(this, "hereeee", Toast.LENGTH_LONG).show()
                mProgressDialog.dismiss()
                when {
                    !result?.items.isNullOrEmpty() -> {
                        developersItem.clear()
                        result?.items?.let {
                            developersItem.addAll(it)
                            developersAdapter.submitList(developersItem)  //updateList(developersItem)
                            Timber.e(result.toString())
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