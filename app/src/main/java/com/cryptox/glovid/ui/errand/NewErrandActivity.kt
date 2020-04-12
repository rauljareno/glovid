package com.cryptox.glovid.ui.errand

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cryptox.glovid.R
import kotlinx.android.synthetic.main.activity_new_errand.*

class NewErrandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_errand)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        create_errand_button.setOnClickListener {
            if (et_desc.text.isNotEmpty()) {
                val intent = Intent(this, ErrandCreatedActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

}
