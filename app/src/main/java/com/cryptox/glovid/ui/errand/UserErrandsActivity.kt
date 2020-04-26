package com.cryptox.glovid.ui.errand

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cryptox.glovid.R
import com.cryptox.glovid.viewModels.orders.OrderType
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class UserErrandsActivity : AppCompatActivity() , HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val orderType = intent.getSerializableExtra("OrderType") as OrderType
        setFragment(UserErrandsFragment(orderType))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (orderType == OrderType.GIVE) {
            supportActionBar?.title = getString(R.string.my_donations)
        } else {
            supportActionBar?.title = getString(R.string.my_asks)
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(android.R.id.content, fragment)
            .commit()
    }

    override fun supportFragmentInjector() = dispatchingAndroidInjector

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}