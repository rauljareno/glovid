package com.cryptox.glovid.ui.order

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.ui.chat.ChatActivity
import com.cryptox.glovid.ui.donation.DonationRequestedActivity
import com.cryptox.glovid.ui.errand.ErrandAcceptedActivity
import com.cryptox.glovid.utils.ImageUtils
import com.cryptox.glovid.utils.getJsonExtra
import com.cryptox.glovid.utils.putExtraJson
import com.cryptox.glovid.viewModels.orders.OrderType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


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
        tvDesc.text = order?.detail

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val barcelona = LatLng(41.385, 2.173)
        googleMap?.addMarker(
            MarkerOptions().position(barcelona)
                .title(order?.user?.name)
                .icon(BitmapDescriptorFactory.fromBitmap(ImageUtils.getBitmapFromVectorDrawable(applicationContext, R.drawable.location)))
        )
        //Move the camera to the user's location and zoom in!
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(barcelona, 18.0f))

        googleMap?.addCircle(
            CircleOptions().center(barcelona).radius(30.0).strokeWidth(0.0f).fillColor(0x5527DEBF)
        )

        //googleMap?.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
    }
}
