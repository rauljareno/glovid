package com.cryptox.glovid.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cryptox.glovid.R
import com.cryptox.glovid.adapters.home.HomeAdapter
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.prefs
import com.cryptox.glovid.ui.donation.NewDonationActivity
import com.cryptox.glovid.ui.errand.ErrandsMapActivity
import com.cryptox.glovid.ui.errand.NewErrandActivity
import com.cryptox.glovid.ui.login.LoginActivity
import com.cryptox.glovid.ui.main.MainActivity
import com.cryptox.glovid.ui.onboarding.OnboardingActivity
import com.cryptox.glovid.ui.profile.ProfileActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // Initializing an empty ArrayList to be filled with animals
    val orders: ArrayList<Order> = ArrayList()

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    public var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val navDrawerTitle = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_title)
        navDrawerTitle.text = prefs.user?.displayName

        val navDrawerSubtitle = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_drawer_subtitle)
        navDrawerSubtitle.text = prefs.user?.userId

        val askButton = findViewById<LinearLayout>(R.id.ask_button)
        askButton.setOnClickListener {
            val intent = Intent(this, NewErrandActivity::class.java)
            startActivity(intent)
        }

        val errandButton = findViewById<LinearLayout>(R.id.errand_button)
        errandButton.setOnClickListener {
            val intent = Intent(this, ErrandsMapActivity::class.java)
            startActivity(intent)
        }

        val donateButton = findViewById<LinearLayout>(R.id.donate_button)
        donateButton.setOnClickListener {
            val intent = Intent(this, NewDonationActivity::class.java)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)
        val recyclerList = findViewById<RecyclerView>(R.id.recyclerList)

        // Loads animals into the ArrayList
        addOrders()

        val layoutManager = LinearLayoutManager(this)

        // Creates a vertical Layout Manager
        recyclerList.layoutManager = layoutManager

        // Access the RecyclerView Adapter and load the data into it
        recyclerList.adapter = HomeAdapter(orders, this)
    }

    override fun onResume() {
        super.onResume()
        if (!prefs.onboarding) {
            prefs.onboarding = true
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_my_ask -> {
                Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_errands -> {
                Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_my_donations -> {
                Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_update -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                prefs.user = null
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
* Fused location provider is used here to get the current/last known location
* Use places api to get more accurate latlong
* */
    private fun getLastKnownLocation() {
        if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
            fusedLocationClient.lastLocation.addOnSuccessListener {
                it?.let {
                    query = it.latitude.toString() + "," + it.longitude.toString()
                    //viewModel.getOrders(query)
                }?:run{
                    Toast.makeText(this, getString(R.string.unable_to_get_current_location), Toast.LENGTH_LONG).show()
                }

            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        val hasPermission = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
        }
        return hasPermission
    }

    // Adds animals to the empty animals ArrayList
    private fun addOrders() {
        orders.add(Order( "Manuel", "Necesito mascarillas", "", 1))
        orders.add(Order("Francisco", "Quiero donar guantes", "", 2))
        orders.add(Order("Paco y Manuel", "Paco quiere donar guantes a Manuel", "",3))
        orders.add(Order( "Paula", "Necesito alguien con quien conversar","",1))
        orders.add(Order( "Rocio", "Quiero donar un reloj", "", 2))
        orders.add(Order("Roger y David", "Roger quiere donar un carrito de bebé a David", "", 3))
        orders.add(Order("Luis", "Necesito huevos", "",1))
        orders.add(Order( "Alex", "Quiero donar comida", "",2))
        orders.add(Order( "Mónica y Oscar", "Mónica quiere donar una chaqueta a Oscar", "",3))
        orders.add(Order( "Mireia y Ernesto", "Mireia quiere donar huevos a Ernesto", "",3))
        orders.add(Order( "Silvia", "Necesito un taladro", "",1))
        orders.add(Order( "Patricia", "Necesito un móvil", "",1))
        orders.add(Order( "Javier", "Quiero donar unas zapatillas", "",2))
        orders.add(Order( "Juan y Lucas", "Juan quiere donar una raqueta a Lucas", "",3))
        orders.add(Order( "Ulises", "Quiero donar un iphone", "",2))

    }
}
