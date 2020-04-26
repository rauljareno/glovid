package com.cryptox.glovid.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cryptox.glovid.ui.login.LoginFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() , HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragment(ProfileFragment())
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


/*class ProfileActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val updateProfileBtn = findViewById<Button>(R.id.update_profile_button)
        updateProfileBtn.setOnClickListener {
            val intent = Intent(this, ProfileUpdatedActivity::class.java)
            startActivity(intent)
            finish()
        }

        val email = findViewById<TextView>(R.id.tv_email)
        email.text = prefs.user?.userId

        val username = findViewById<EditText>(R.id.username)
        username.setText(prefs.user?.displayName)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment: SupportMapFragment? = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val barcelona = LatLng(41.385, 2.173)
        googleMap?.addMarker(
            MarkerOptions().position(barcelona)
                .title(String.format("Casa de %s", prefs.user?.displayName))
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )
        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 18.0f))

        googleMap?.addCircle(
            CircleOptions().center(barcelona).radius(30.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        )

        //googleMap?.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
    }
}*/
