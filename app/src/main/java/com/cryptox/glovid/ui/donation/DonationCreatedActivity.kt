package com.cryptox.glovid.ui.donation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.cryptox.glovid.R

class DonationCreatedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_donation_created)
        supportActionBar?.hide()

        val closeBtn = findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            finish()
        }
    }
}
