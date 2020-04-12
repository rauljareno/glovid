package com.cryptox.glovid.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.cryptox.glovid.R
import com.cryptox.glovid.prefs
import com.cryptox.glovid.ui.home.HomeActivity
import com.cryptox.glovid.ui.login.LoginActivity
import com.cryptox.glovid.ui.signup.SignUpActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class MainActivity : FragmentActivity() {

    private lateinit var callbackManager : CallbackManager

    private val REGISTER_REQUEST_CODE = 1
    private val LOGIN_REQUEST_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val fb = findViewById<Button>(R.id.fb_button)
        fb.setOnClickListener {
            Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            //LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }

        val googleBtn = findViewById<Button>(R.id.google_button)
        googleBtn.setOnClickListener {
            Toast.makeText(this, "Esta funcionalidad no esta disponible aún, disculpe las molestias", Toast.LENGTH_SHORT).show()
            //LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
        }

        val loginButton = findViewById<TextView>(R.id.login_button)
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, LOGIN_REQUEST_CODE)
        }

        val registerButton = findViewById<TextView>(R.id.register_button)
        registerButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivityForResult(intent, REGISTER_REQUEST_CODE)
        }

        /*callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    // Handle success
                    // TODO: go to dashboard
                }

                override fun onCancel() {}
                override fun onError(exception: FacebookException) {
                    //showLoginFailed(R.string.com_facebook_loginview_log_in_button)
                }
            }
        )*/

        //val gso: GoogleSignInOptions = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*if (requestCode == REGISTER_REQUEST_CODE || requestCode == LOGIN_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (prefs.user != null) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }*/
        //callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        if (prefs.user != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}
