package com.cryptox.glovid.ui.login

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.databinding.FragmentLoginBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.prefs
import com.cryptox.glovid.viewModels.login.LoginViewModelImpl
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


open class LoginFragment : Fragment(), Injectable {

    private val TAG = LoginFragment::class.java.simpleName

    private lateinit var viewModel: LoginViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentLoginBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_login,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        //viewModel = ViewModelProviders.of(this, viewModelFactory)
         //   .get(LoginViewModelImpl::class.java)

        viewModel.loginFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        viewModel.login().observe(viewLifecycleOwner, Observer {
            val user = it ?: return@Observer
            loading.visibility = View.GONE
            if (user.token.isEmpty()) {
                showLoginFailed(R.string.login_failed)//loginResult.error)
            } else {
                prefs.user = user
                viewModel.callProfileAPI()
                //activity!!.setResult(Activity.RESULT_OK)
                //Complete and destroy register activity once successful
                //activity!!.finish()
            }
        })

        viewModel.profile().observe(viewLifecycleOwner, Observer {
            val user = it ?: return@Observer
            loading.visibility = View.GONE
            if (user.token.isEmpty()) {
                showLoginFailed(R.string.login_failed)//loginResult.error)
            } else {
                prefs.user = user
                activity!!.setResult(Activity.RESULT_OK)
                //Complete and destroy register activity once successful
                activity!!.finish()
            }
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showLoginFailed(R.string.login_failed)//loginResult.error)
        })

        username.afterTextChanged {
            viewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                viewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.callLoginAPI(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                viewModel.callLoginAPI(username.text.toString(), password.text.toString())
            }
        }
    }


    private fun setViewModel(){
        viewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(LoginViewModelImpl::class.java)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}