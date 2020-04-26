package com.cryptox.glovid.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.category.SubcategoryAdapter
import com.cryptox.glovid.data.model.CategoryEnum
import com.cryptox.glovid.databinding.FragmentErrandCategoryBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.viewModels.orders.OrderType
import kotlinx.android.synthetic.main.fragment_errand_category.*


open class SubcategoryFragment(orderType: OrderType, categoryEnum: CategoryEnum) : Fragment(), Injectable {

    private val category = categoryEnum
    private val orderType = orderType

    private val TAG = SubcategoryFragment::class.java.simpleName

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
        val adapter = SubcategoryAdapter(orderType, category)

        // Set the grid view adapter
        grid_view.adapter = adapter

        // Configure the grid view
        grid_view.numColumns = 3
        grid_view.horizontalSpacing = 15
        grid_view.verticalSpacing = 15
        grid_view.stretchMode = GridView.STRETCH_COLUMN_WIDTH
    }
}