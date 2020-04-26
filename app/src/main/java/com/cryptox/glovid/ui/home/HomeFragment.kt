package com.cryptox.glovid.ui.home

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.home.HomeAdapter
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.databinding.FragmentHomeBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.prefs
import com.cryptox.glovid.ui.donation.NewDonationActivity
import com.cryptox.glovid.ui.category.CategoryActivity
import com.cryptox.glovid.ui.errand.ErrandsMapActivity
import com.cryptox.glovid.ui.errand.UserErrandsActivity
import com.cryptox.glovid.ui.main.MainActivity
import com.cryptox.glovid.ui.onboarding.OnboardingActivity
import com.cryptox.glovid.ui.profile.ProfileActivity
import com.cryptox.glovid.viewModels.orders.OrderStatus
import com.cryptox.glovid.viewModels.orders.OrderType
import com.cryptox.glovid.viewModels.orders.OrdersViewModelImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


open class HomeFragment : Fragment(), Injectable, NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var needRefresh = false
    }

    private val TAG = HomeFragment::class.java.simpleName

    private lateinit var viewModel: OrdersViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentHomeBinding

    // Initializing an empty ArrayList to be filled with animals
    var orders: ArrayList<Order> = ArrayList()

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public var query = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_home,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        (activity as AppCompatActivity).setSupportActionBar(toolbar)


        val navDrawerTitle = nav_view.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_title)
        navDrawerTitle.text = prefs.user?.displayName

        val navDrawerSubtitle = nav_view.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_subtitle)
        navDrawerSubtitle.text = prefs.user?.userId

        ask_button.setOnClickListener {
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra("OrderType", OrderType.ASK)
            startActivity(intent)
        }

        errand_button.setOnClickListener {
            val intent = Intent(activity, ErrandsMapActivity::class.java)
            startActivity(intent)
        }

        donate_button.setOnClickListener {
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra("OrderType", OrderType.GIVE)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
            activity, drawer_layout, toolbar, 0, 0
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        //** Set the colors of the Pull To Refresh View
        itemsSwipeToRefresh.setProgressBackgroundColorSchemeColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        itemsSwipeToRefresh.setColorSchemeColors(Color.WHITE)

        itemsSwipeToRefresh.setOnRefreshListener {
            orders.clear()
            viewModel.callSearchOrdersAPI(listOf(OrderType.ASK, OrderType.GIVE), listOf(OrderStatus.PENDING, OrderStatus.ACCEPTED))
        }

        val layoutManager = LinearLayoutManager(activity)

        // Creates a vertical Layout Manager
        recyclerList.layoutManager = layoutManager

        // Access the RecyclerView Adapter and load the data into it
        recyclerList.adapter = HomeAdapter(orders, activity!!)

        viewModel.searchOrders().observe(viewLifecycleOwner, Observer {
            val orderList = it ?: return@Observer
            orders = ArrayList(orderList)
            recyclerList.adapter = HomeAdapter(orders, activity!!)
            itemsSwipeToRefresh.isRefreshing = false
            //loading.visibility = View.GONE
            /*if (user.token.isEmpty()) {
                showSignUpFailed(R.string.signup_failed)//loginResult.error)
            } else {
                prefs.user = user
                activity!!.setResult(Activity.RESULT_OK)
                //Complete and destroy register activity once successful
                activity!!.finish()
            }*/
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            //loading.visibility = View.GONE
            showSearchOrdersFailed(R.string.signup_failed)//loginResult.error)
            itemsSwipeToRefresh.isRefreshing = false
        })

        itemsSwipeToRefresh.isRefreshing = true
        viewModel.callSearchOrdersAPI(listOf(OrderType.ASK, OrderType.GIVE), listOf(OrderStatus.PENDING, OrderStatus.ACCEPTED))
    }

    override fun onResume() {
        super.onResume()
        if (!prefs.onboarding) {
            prefs.onboarding = true
            val intent = Intent(activity, OnboardingActivity::class.java)
            startActivity(intent)
        }
        if (needRefresh) {
            needRefresh = false
            itemsSwipeToRefresh.isRefreshing = true
            viewModel.callSearchOrdersAPI(listOf(OrderType.ASK, OrderType.GIVE), listOf(OrderStatus.PENDING, OrderStatus.ACCEPTED))
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_ask -> {
                val intent = Intent(activity, UserErrandsActivity::class.java)
                intent.putExtra("OrderType", OrderType.ASK)
                startActivity(intent)
            }
            /*R.id.nav_errands -> {
                Toast.makeText(activity, getString(R.string.not_available_yet), Toast.LENGTH_SHORT).show()
            }*/
            R.id.nav_my_donations -> {
                val intent = Intent(activity, UserErrandsActivity::class.java)
                intent.putExtra("OrderType", OrderType.GIVE)
                startActivity(intent)
            }
            R.id.nav_update -> {
                val intent = Intent(activity, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                prefs.user = null
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setViewModel(){
        viewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(OrdersViewModelImpl::class.java)
    }

    private fun showSearchOrdersFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}