package com.cryptox.glovid.ui.errand

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import com.cryptox.glovid.R

class ErrandCreatedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_errand_created)
        supportActionBar?.hide()

        val closeBtn = findViewById<ImageView>(R.id.close_btn)
        closeBtn.setOnClickListener {
            finish()
        }
    }
}
