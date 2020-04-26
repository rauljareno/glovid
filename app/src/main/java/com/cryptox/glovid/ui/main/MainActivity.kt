package com.cryptox.glovid.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.cryptox.glovid.R
import com.cryptox.glovid.prefs
import com.cryptox.glovid.ui.home.HomeActivity
import com.cryptox.glovid.ui.login.LoginActivity
import com.cryptox.glovid.ui.signup.SignUpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.location.*

class MainActivity : FragmentActivity() {


    //private lateinit var callbackManager : CallbackManager

    private val REGISTER_REQUEST_CODE = 1
    private val LOGIN_REQUEST_CODE = 2

    private val LOCATION_PERMISSIONS_ID = 42

    lateinit var mFusedLocationClient: FusedLocationProviderClient

    var registerClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val fb = findViewById<Button>(R.id.fb_button)
        fb.setOnClickListener {
            Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            //LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }

        val googleBtn = findViewById<Button>(R.id.google_button)
        googleBtn.setOnClickListener {
            Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            //LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }

        val loginButton = findViewById<TextView>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, LOGIN_REQUEST_CODE)
        }

        val registerButton = findViewById<TextView>(R.id.register_button)
        registerButton.setOnClickListener {
            registerClicked = true
            getLastLocation()
        }

        /*callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // Handle success
                    // TODO: go to dashboard
                }

                override fun onCancel() {}
                override fun onError(exception: FacebookException) {
                    //showLoginFailed(R.string.com_facebook_loginview_log_in_button)
                }
            }
        )*/

        //val gso: GoogleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == REGISTER_REQUEST_CODE || requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (prefs.user != null) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }*/
        //callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (prefs.user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
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
                        if (registerClicked) {
                            val intent = Intent(this, SignUpActivity::class.java)
                            intent.putExtra("Location", location)
                            startActivityForResult(intent, REGISTER_REQUEST_CODE)
                        }
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
            if (registerClicked) {
                val intent = Intent(applicationContext, SignUpActivity::class.java)
                intent.putExtra("Location", mLastLocation)
                startActivityForResult(intent, REGISTER_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSIONS_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }
}
