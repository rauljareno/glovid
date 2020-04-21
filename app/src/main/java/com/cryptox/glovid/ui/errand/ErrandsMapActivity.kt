package com.cryptox.glovid.ui.errand

import android.R.attr.radius
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.ui.order.OrderDetailsActivity
import com.cryptox.glovid.utils.ImageUtils
import com.cryptox.glovid.utils.putExtraJson
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class ErrandsMapActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_errands_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recados cerca de ti"

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
        val barcelona1 = LatLng(41.385547, 2.171959)
        val barcelona2 = LatLng(41.385136, 2.172227)
        val barcelona3 = LatLng(41.385127, 2.171725)
        val barcelona4 = LatLng(41.385650, 2.172784)
        val barcelona5 = LatLng(41.384773, 2.173878)
        val barcelona7 = LatLng(41.384443, 2.172612)
        val barcelona8 = LatLng(41.385447, 2.173515)
        googleMap?.addMarker(
            MarkerOptions().position(barcelona)
                .title("Tu localización")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.errand)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona1)
                .title("Francisco")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona2)
                .title("Maria")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona3)
                .title("Paula")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona4)
                .title("Carlos")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona5)
                .title("Rocío")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona7)
                .title("Juan")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.addMarker(
            MarkerOptions().position(barcelona8)
                .title("Roger")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )

        googleMap?.setOnInfoWindowClickListener {
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtraJson("ORDER", Order( 1, "Necesito mascarillas", "", "",null))
            startActivity(intent)
            finish()
        }

        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 17.0f))

        googleMap?.addCircle(
            CircleOptions().center(barcelona).radius(120.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        )
    }
}
