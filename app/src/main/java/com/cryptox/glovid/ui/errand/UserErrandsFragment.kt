package com.cryptox.glovid.ui.errand

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.home.HomeAdapter
import com.cryptox.glovid.adapters.order.UserOrderAdapter
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.databinding.FragmentNewErrandBinding
import com.cryptox.glovid.databinding.FragmentUserOrdersBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.viewModels.orders.OrderType
import com.cryptox.glovid.viewModels.orders.OrdersViewModelImpl
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_user_orders.*
import kotlinx.android.synthetic.main.fragment_user_orders.itemsSwipeToRefresh
import kotlinx.android.synthetic.main.fragment_user_orders.recyclerList
import javax.inject.Inject

open class UserErrandsFragment(orderType: OrderType) : Fragment(), Injectable {

    // Initializing an empty ArrayList to be filled with animals
    var orders: ArrayList<Order> = ArrayList()

    private var orderType = orderType

    private val TAG = UserErrandsFragment::class.java.simpleName

    private lateinit var viewModel: OrdersViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentUserOrdersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_orders, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        //** Set the colors of the Pull To Refresh View
        itemsSwipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        itemsSwipeToRefresh.setColorSchemeColors(Color.WHITE)

        itemsSwipeToRefresh.setOnRefreshListener {
            orders.clear()
            viewModel.callGetUserOrdersAPI()
        }

        val layoutManager = LinearLayoutManager(activity)

        // Creates a vertical Layout Manager
        recyclerList.layoutManager = layoutManager

        // Access the RecyclerView Adapter and load the data into it
        recyclerList.adapter = UserOrderAdapter(orders, activity!!)

        viewModel.getUserOrders(orderType).observe(viewLifecycleOwner, Observer {
            val orderList = it ?: return@Observer
            orders = ArrayList(orderList)
            recyclerList.adapter = UserOrderAdapter(orders, activity!!)
            itemsSwipeToRefresh.isRefreshing = false
            /*val intent = Intent(activity!!, ErrandCreatedActivity::class.java)
            startActivity(intent)
            HomeFragment.needRefresh = true
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()*/
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            //loading.visibility = View.GONE
            showNewErrandFailed(R.string.new_ask_failed)//loginResult.error)
            itemsSwipeToRefresh.isRefreshing = false
        })

        orders.clear()
        itemsSwipeToRefresh.isRefreshing = true
        viewModel.callGetUserOrdersAPI()
    }

    private fun setViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(OrdersViewModelImpl::class.java)
    }

    private fun showNewErrandFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}