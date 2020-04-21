package com.cryptox.glovid.ui.signup

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cryptox.glovid.R
import com.cryptox.glovid.databinding.FragmentSignupBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.location.LocationConstants
import com.cryptox.glovid.prefs
import com.cryptox.glovid.utils.ImageUtils
import com.cryptox.glovid.viewModels.signup.SignUpViewModelImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_signup.*
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


open class SignUpFragment : Fragment(), Injectable, OnMapReadyCallback {

    private val TAG = SignUpFragment::class.java.simpleName

    private lateinit var signUpViewModel: SignUpViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentSignupBinding

    private var mMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private var countryCode: String? = null
    private var postalCode: String? = null
    private var marker: Marker? = null
    lateinit var geocoder: Geocoder
    private var selectedAddress: String? = null

    //internal var mReceiver: AddressResultReceiver(null)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_signup,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = signUpViewModel
        dataBinding.lifecycleOwner = this

        geocoder = Geocoder(context, Locale.getDefault())

        signUpViewModel.signUpFormState.observe(viewLifecycleOwner, Observer {
            val signupState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.nameError != null) {
                name.error = getString(signupState.nameError)
            }
            if (signupState.emailError != null) {
                email.error = getString(signupState.emailError)
            }
            if (signupState.phoneNumberError != null) {
                phoneNumber.error = getString(signupState.phoneNumberError)
            }
            if (signupState.passwordError != null) {
                password.error = getString(signupState.passwordError)
            }
            if (signupState.addressError != null) {
                addressEt.error = getString(signupState.addressError)
            }
        })

        signUpViewModel.register().observe(viewLifecycleOwner, Observer {
            val user = it ?: return@Observer
            loading.visibility = View.GONE
            if (user.token.isEmpty()) {
                showSignUpFailed(R.string.signup_failed)//loginResult.error)
            } else {
                prefs.user = user
                activity!!.setResult(Activity.RESULT_OK)
                //Complete and destroy register activity once successful
                activity!!.finish()
            }
        })

        signUpViewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showSignUpFailed(R.string.signup_failed)//loginResult.error)
        })

        name.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                context!!,
                name.text.toString(),
                email.text.toString(),
                countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(),
                password.text.toString(),
                addressEt.text.toString()
            )
        }

        email.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                context!!,
                name.text.toString(),
                email.text.toString(),
                countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(),
                password.text.toString(),
                addressEt.text.toString()
            )
        }

        countryCodePicker.hideNameCode(true)

        phoneNumber.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                context!!,
                name.text.toString(),
                email.text.toString(),
                countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(),
                password.text.toString(),
                addressEt.text.toString()
            )
        }

        password.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                context!!,
                name.text.toString(),
                email.text.toString(),
                countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(),
                password.text.toString(),
                addressEt.text.toString()
            )
        }

        addressEt.apply {
            afterTextChanged {
                val value: String = addressEt.text.toString()
                // Do something with value!
                Log.d("value", value)
                try {
                    var addresses: List<Address?> = ArrayList()
                    try {
                        addresses = geocoder.getFromLocationName(value, 5)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        if (address != null) {
                            val sb = StringBuilder()
                            for (i in 0 until address.maxAddressLineIndex + 1) {
                                sb.append(address.getAddressLine(i).trimIndent()
                                )
                            }
                            selectedAddress = sb.toString()
                            latLng = LatLng(address.latitude, address.longitude)
                            countryCode = address.countryCode
                            postalCode = address.postalCode

                            //remove previously placed Marker
                            if (marker != null) {
                                marker?.remove()
                            }

                            //place marker where user just clicked
                            marker = mMap?.addMarker(
                                MarkerOptions().position(latLng!!)
                                    .icon(
                                        BitmapDescriptorFactory.fromBitmap(
                                            ImageUtils.getBitmapFromVectorDrawable(
                                                activity!!.applicationContext,
                                                R.drawable.location
                                            )
                                        )
                                    )
                            )
                            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f))
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                signUpViewModel.signUpDataChanged(
                    context!!,
                    name.text.toString(),
                    email.text.toString(),
                    countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(),
                    password.text.toString(),
                    addressEt.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        addressEt.setText(selectedAddress)
                }
                false
            }

            addressEt.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    addressEt.setText(selectedAddress)
                }
            }
        }

        signup.setOnClickListener {
            loading.visibility = View.VISIBLE
            signUpViewModel.callRegisterAPI(name.text.toString(), email.text.toString(), countryCodePicker.selectedCountryCodeWithPlus + phoneNumber.text.toString(), password.text.toString(), latLng!!, countryCode!!, postalCode!!)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        val barcelona = LatLng(41.385, 2.173)
        googleMap?.uiSettings?.isMapToolbarEnabled = false
        googleMap?.setOnMapClickListener{ point -> //save current location
            var addresses: List<Address?> = ArrayList()
            try {
                addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                if (address != null) {
                    val sb = StringBuilder()
                    for (i in 0 until address.maxAddressLineIndex + 1) {
                        sb.append(address.getAddressLine(i).trimIndent()
                        )
                    }
                    addressEt.setText(sb.toString())
                    latLng = point
                    countryCode = address.countryCode
                    postalCode = address.postalCode
                    //Toast.makeText(this@MapsActivity, sb.toString(), Toast.LENGTH_LONG).show()
                }

                //remove previously placed Marker
                if (marker != null) {
                    marker?.remove()
                }

                //place marker where user just clicked
                marker = googleMap.addMarker(
                    MarkerOptions().position(point)
                        .icon(
                            BitmapDescriptorFactory.fromBitmap(
                                ImageUtils.getBitmapFromVectorDrawable(
                                    activity!!.applicationContext,
                                    R.drawable.location
                                )
                            )
                        )
                )
            }
        }
        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 18.0f))
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    private fun setViewModel(){
        signUpViewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(SignUpViewModelImpl::class.java)
    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

internal class AddressResultReceiver(handler: Handler?) : ResultReceiver(handler) {
    override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
        if (resultCode == LocationConstants.SUCCESS_RESULT) {
            val address =
                resultData.getParcelable<Address>(LocationConstants.RESULT_ADDRESS)
            /*runOnUiThread(Runnable { /*progressBar.setVisibility(View.INVISIBLE);
                    infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                            "Longitude: " + address.getLongitude() + "\n" +
                            "Address: " + resultData.getString(Constants.RESULT_DATA_KEY));*/
            })*/
        } else {
            val error = resultData.getString(LocationConstants.RESULT_DATA_KEY)
            /*runOnUiThread(Runnable {
                //progressBar.setVisibility(View.INVISIBLE);
                infoText.setText();
            })*/
        }
    }
}

