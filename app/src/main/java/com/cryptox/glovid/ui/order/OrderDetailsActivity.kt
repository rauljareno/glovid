package com.cryptox.glovid.ui.order

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.ui.donation.DonationRequestedActivity
import com.cryptox.glovid.ui.errand.ErrandAcceptedActivity
import com.cryptox.glovid.utils.getJsonExtra
import com.cryptox.glovid.utils.putExtraJson
import com.cryptox.glovid.viewModels.orders.OrderType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class OrderDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_order)

        order = intent.getJsonExtra("ORDER", Order::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (order?.type == OrderType.GIVE.toString()) {
            supportActionBar?.title = getString(R.string.donation)
        } else {
            supportActionBar?.title = getString(R.string.errand)
        }

        val tvName = findViewById<TextView>(R.id.tv_name)
        tvName.text = order?.user?.name

        val tvDesc = findViewById<TextView>(R.id.tv_description)
        if (order?.type == OrderType.GIVE.toString()) {
            tvDesc.text = String.format(getString(R.string.offers), order?.detail)
        } else {
            tvDesc.text = String.format(getString(R.string.needs), order?.detail)
        }

        val tvLocation = findViewById<TextView>(R.id.tv_location)

        val acceptErrandBtn = findViewById<Button>(R.id.accept_errand_button)
        if (order?.type == OrderType.GIVE.toString()) {
            acceptErrandBtn.text = getString(R.string.request_donation)
        } else {
            acceptErrandBtn.text = getString(R.string.accept_errand)
        }
        acceptErrandBtn.setOnClickListener {
            if (order?.type == OrderType.GIVE.toString()) {
                val intent = Intent(this, DonationRequestedActivity::class.java)
                intent.putExtraJson("ORDER", order!!)
                startActivity(intent)
            } else {
                val intent = Intent(this, ErrandAcceptedActivity::class.java)
                intent.putExtraJson("ORDER", order!!)
                startActivity(intent)
            }
            finish()
        }

        if (order?.user?.latitude != null && order?.user?.longitude != null) {
            var addresses: List<Address?> = ArrayList()
            try {
                val geocoder = Geocoder(this, Locale.getDefault())
                addresses = geocoder.getFromLocation(order?.user?.latitude!!, order?.user?.longitude!!, 1)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (addresses.isNotEmpty()) {
                val address = addresses[0]
                if (address != null) {
                  tvLocation.text = String.format("%s, %s", address.postalCode, address.locality)
                }
            }
        }

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

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.order_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.itemId
        if (id == R.id.action_name) {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtraJson("ORDER", order!!)
            startActivity(intent)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }*/

    override fun onMapReady(googleMap: GoogleMap?) {
        val point = order?.user?.latitude?.let { order?.user?.longitude?.let { it1 ->
            LatLng(it, it1) } }
        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 18.0f))

        googleMap?.addCircle(
            CircleOptions().center(point).radius(30.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        )
    }
}
