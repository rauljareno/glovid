package com.cryptox.glovid.ui.profile

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.databinding.FragmentProfileBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.prefs
import com.cryptox.glovid.utils.ImageUtils
import com.cryptox.glovid.viewModels.profile.ProfileViewModelImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.loading
import java.io.IOException
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


open class ProfileFragment : Fragment(), Injectable, OnMapReadyCallback {

    private val TAG = ProfileFragment::class.java.simpleName

    private lateinit var viewModel: ProfileViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentProfileBinding

    private var mMap: GoogleMap? = null
    private var latLng: LatLng? = null
    private var marker: Marker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_profile,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        val geocoder = Geocoder(context, Locale.getDefault())

        viewModel.profile().observe(viewLifecycleOwner, Observer {
            val user = it ?: return@Observer
            prefs.user = User(user.email!!, user.name!!, prefs.user!!.token)
            latLng = user.latitude?.let { it1 -> user.longitude?.let { it2 -> LatLng(it1, it2) } }
            tv_email.text = prefs.user?.userId
            username.setText(prefs.user?.displayName)

            if (user.latitude != null && user.longitude != null) {
                var addresses: List<Address?> = ArrayList()
                try {
                    addresses = geocoder.getFromLocation(user.latitude!!, user.longitude!!, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (addresses.isNotEmpty()) {
                    val address = addresses[0]
                    if (address != null) {
                        val sb = StringBuilder()
                        for (i in 0 until address.maxAddressLineIndex + 1) {
                            sb.append(
                                address.getAddressLine(i).trimIndent()
                            )
                        }
                        location.setText(sb.toString())
                    }
                }

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
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showProfileFailed(R.string.login_failed)//loginResult.error)
        })

        update_profile_button.setOnClickListener {
            val intent = Intent(activity!!, ProfileUpdatedActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        tv_email.text = prefs.user?.userId
        username.setText(prefs.user?.displayName)

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        val mapFragment: SupportMapFragment? = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        viewModel.callProfileAPI()
    }


    private fun setViewModel(){
        viewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(ProfileViewModelImpl::class.java)
    }

    private fun showProfileFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
    }
}