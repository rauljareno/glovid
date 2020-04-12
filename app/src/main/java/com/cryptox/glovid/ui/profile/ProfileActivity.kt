package com.cryptox.glovid.ui.profile

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.cryptox.glovid.R
import com.cryptox.glovid.prefs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ProfileActivity : AppCompatActivity(), OnMapReadyCallback {

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
                .icon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )
        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 18.0f))

        googleMap?.addCircle(
            CircleOptions().center(barcelona).radius(30.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        )

        //googleMap?.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
    }

    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
        var drawable =
            ContextCompat.getDrawable(context!!, drawableId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
