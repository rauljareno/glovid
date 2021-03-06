package com.cryptox.glovid.ui.errand

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.ui.chat.ChatActivity
import com.cryptox.glovid.utils.getJsonExtra
import com.cryptox.glovid.utils.putExtraJson

class ErrandAcceptedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_errand_accepted)
        supportActionBar?.hide()

        val order = intent.getJsonExtra("ORDER", Order::class.java)

        val closeBtn = findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            finish()
        }

        val contactBtn = findViewById<Button>(R.id.contact_user_btn)
        contactBtn.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtraJson("ORDER", order!!)
            startActivity(intent)
            finish()
        }

        contactBtn.text = getString(R.string.action_contact_to, order!!.user?.name)
    }
}
