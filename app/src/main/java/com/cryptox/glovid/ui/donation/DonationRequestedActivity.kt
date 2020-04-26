package com.cryptox.glovid.ui.donation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.ui.chat.ChatActivity
import com.cryptox.glovid.utils.getJsonExtra
import com.cryptox.glovid.utils.putExtraJson

class DonationRequestedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_donation_requested)
        supportActionBar?.hide()

        val order = intent.getJsonExtra("ORDER", Order::class.java)

        val closeBtn = findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            finish()
        }

        val contactBtn = findViewById<Button>(R.id.contact_user_btn)
        contactBtn.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone="+ order?.user?.phoneNumber
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
            finish()
        }

        contactBtn.text = getString(R.string.action_contact_to, order!!.user?.name)
    }
}
