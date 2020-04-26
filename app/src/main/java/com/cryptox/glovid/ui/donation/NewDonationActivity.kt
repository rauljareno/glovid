package com.cryptox.glovid.ui.donation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.CategoryEnum
import com.cryptox.glovid.data.model.SubcategoryEnum
import com.cryptox.glovid.ui.chat.ChatActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_new_donation.*
import javax.inject.Inject


class NewDonationActivity : AppCompatActivity() , HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val category = intent.getSerializableExtra("Category") as CategoryEnum?
        val subcategory = intent.getSerializableExtra("Subcategory") as SubcategoryEnum?
        setFragment(NewDonationFragment(category, subcategory))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
