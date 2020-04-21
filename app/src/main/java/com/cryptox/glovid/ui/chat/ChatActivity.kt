package com.cryptox.glovid.ui.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.utils.getJsonExtra

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        val order = intent.getJsonExtra("ORDER", Order::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = order?.user?.id
        supportActionBar?.subtitle = "últ. vez 11:34"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
