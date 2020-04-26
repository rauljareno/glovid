package com.cryptox.glovid.ui.errand

import android.Manifest
import android.R.attr.radius
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.ui.order.OrderDetailsActivity
import com.cryptox.glovid.ui.signup.SignUpActivity
import com.cryptox.glovid.utils.ImageUtils
import com.cryptox.glovid.utils.putExtraJson
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*


class ErrandsMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSIONS_ID = 42

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    private var mMap: GoogleMap? = null

    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_errands_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Recados cerca de ti"

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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
        mMap = googleMap

        getLastLocation()
        //val barcelona = LatLng(41.385, 2.173)
        /*val barcelona1 = LatLng(41.385547, 2.171959)
        val barcelona2 = LatLng(41.385136, 2.172227)
        val barcelona3 = LatLng(41.385127, 2.171725)
        val barcelona4 = LatLng(41.385650, 2.172784)
        val barcelona5 = LatLng(41.384773, 2.173878)
        val barcelona7 = LatLng(41.384443, 2.172612)
        val barcelona8 = LatLng(41.385447, 2.173515)*/
        /*googleMap?.addMarker(
            MarkerOptions().position(barcelona)
                .title("Tu localización")
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.errand)))
        )*/

        /*googleMap?.addMarker(
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
        }*/

        //Move the camera to the user's location and zoom in!
        //googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 17.0f))

        //googleMap?.addCircle(
        //    CircleOptions().center(barcelona).radius(120.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        //)
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSIONS_ID
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        var latLng = LatLng(location.latitude, location.longitude)
                        //remove previously placed Marker
                        if (marker != null) {
                            marker?.remove()
                        }

                        //place marker where user just clicked
                        marker = mMap?.addMarker(
                            MarkerOptions().position(latLng)
                                .title("Tu localización")
                                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.errand)))
                        )

                        //Move the camera to the user's location and zoom in!
                        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))

                        mMap?.addCircle(
                            CircleOptions().center(latLng).radius(120.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
                        )
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            var latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            //remove previously placed Marker
            if (marker != null) {
                marker?.remove()
            }

            //place marker where user just clicked
            marker = mMap?.addMarker(
                MarkerOptions().position(latLng)
                    .title("Tu localización")
                    .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.errand)))
            )

            //Move the camera to the user's location and zoom in!
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))

            mMap?.addCircle(
                CircleOptions().center(latLng).radius(120.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSIONS_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
                getLastLocation()
            }
        }
    }
}
