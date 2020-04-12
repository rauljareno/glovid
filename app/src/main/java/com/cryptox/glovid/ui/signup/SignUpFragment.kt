package com.cryptox.glovid.ui.signup

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
import com.cryptox.glovid.BaseApplication
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.databinding.FragmentSignupBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.prefs
import com.cryptox.glovid.utils.Prefs
import com.cryptox.glovid.viewModels.signup.SignUpViewModelImpl
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_signup.*
import javax.inject.Inject


open class SignUpFragment : Fragment(), Injectable {

    private val TAG = SignUpFragment::class.java.simpleName

    private lateinit var signUpViewModel: SignUpViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentSignupBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_signup,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = signUpViewModel
        dataBinding.lifecycleOwner = this

        signUpViewModel.signUpFormState.observe(viewLifecycleOwner, Observer {
            val signupState = it ?: return@Observer

            // disable login button unless both username / password is valid
            signup.isEnabled = signupState.isDataValid

            if (signupState.nameError != null) {
                name.error = getString(signupState.nameError)
            }
            if (signupState.emailError != null) {
                email.error = getString(signupState.emailError)
            }
            if (signupState.passwordError != null) {
                password.error = getString(signupState.passwordError)
            }
        })

        signUpViewModel.register().observe(viewLifecycleOwner, Observer {
            val user = it ?: return@Observer
            loading.visibility = View.GONE
            if (user.token.isEmpty()) {
                showSignUpFailed(R.string.signup_failed)//loginResult.error)
            } else {
                prefs.user = user
                activity!!.setResult(Activity.RESULT_OK)
                //Complete and destroy register activity once successful
                activity!!.finish()
            }
        })

        signUpViewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showSignUpFailed(R.string.signup_failed)//loginResult.error)
        })

        name.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        email.afterTextChanged {
            signUpViewModel.signUpDataChanged(
                name.text.toString(),
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                signUpViewModel.signUpDataChanged(
                    name.text.toString(),
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        signUpViewModel.callRegisterAPI(
                            name.text.toString(),
                            email.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            signup.setOnClickListener {
                loading.visibility = View.VISIBLE
                signUpViewModel.callRegisterAPI(name.text.toString(), email.text.toString(), password.text.toString())
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setLoadingAnimation()
        //setRecycler()
        //observeResponse()
        //setListener()
    }


    private fun setViewModel(){
        signUpViewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(SignUpViewModelImpl::class.java)
    }



    private fun updateUiWithUser(model: User) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            context,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showSignUpFailed(@StringRes errorString: Int) {
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