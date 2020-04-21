package com.cryptox.glovid.ui.errand

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.home.ErrandCategoryAdapter
import com.cryptox.glovid.databinding.FragmentErrandCategoryBinding
import com.cryptox.glovid.databinding.FragmentNewErrandBinding
import com.cryptox.glovid.di.Injectable
import kotlinx.android.synthetic.main.fragment_errand_category.*
import kotlinx.android.synthetic.main.fragment_signup.loading
import javax.inject.Inject


open class ErrandCategoryFragment : Fragment(), Injectable {

    private val TAG = ErrandCategoryFragment::class.java.simpleName

    lateinit var dataBinding: FragmentErrandCategoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_errand_category, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBinding.lifecycleOwner = this

        // Get an instance of base adapter
        val adapter = ErrandCategoryAdapter()

        // Set the grid view adapter
        grid_view.adapter = adapter

        // Configure the grid view
        grid_view.numColumns = 3
        grid_view.horizontalSpacing = 15
        grid_view.verticalSpacing = 15
        grid_view.stretchMode = GridView.STRETCH_COLUMN_WIDTH
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setLoadingAnimation()
        //setRecycler()
        //observeResponse()
        //setListener()
    }

    private fun showNewErrandFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}