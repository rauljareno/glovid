package com.cryptox.glovid.ui.errand

import android.app.Activity
import android.content.Intent
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
import com.cryptox.glovid.databinding.FragmentNewErrandBinding
import com.cryptox.glovid.databinding.FragmentSignupBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.prefs
import com.cryptox.glovid.utils.Prefs
import com.cryptox.glovid.viewModels.orders.OrderType
import com.cryptox.glovid.viewModels.orders.OrdersViewModelImpl
import com.cryptox.glovid.viewModels.signup.SignUpViewModelImpl
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_new_errand.*
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.fragment_signup.loading
import javax.inject.Inject


open class NewErrandFragment : Fragment(), Injectable {

    private val TAG = NewErrandFragment::class.java.simpleName

    private lateinit var viewModel: OrdersViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentNewErrandBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_new_errand,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        viewModel.newErrandFormState.observe(viewLifecycleOwner, Observer {
            val newErrandState = it ?: return@Observer

            // disable new errand button unless description is valid
            create_errand_button.isEnabled = newErrandState.isDataValid

            if (newErrandState.descError != null) {
                et_desc.error = getString(newErrandState.descError)
            }
        })

        viewModel.createOrder().observe(viewLifecycleOwner, Observer {
            val order = it ?: return@Observer
            loading.visibility = View.GONE
            val intent = Intent(activity!!, ErrandCreatedActivity::class.java)
            startActivity(intent)
            activity!!.finish()
            //if (order == null) {
            //    showSignUpFailed(R.string.signup_failed)//loginResult.error)
            //} else {
            //prefs.user = user
            //activity!!.setResult(Activity.RESULT_OK)
            //Complete and destroy register activity once successful
            //activity!!.finish()
            //}
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showNewErrandFailed(R.string.new_ask_failed)//loginResult.error)
        })

        et_desc.apply {
            afterTextChanged {
                viewModel.newErrandDataChanged(
                    et_desc.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        viewModel.callCreateOrderAPI(
                            et_desc.text.toString(),
                            OrderType.ASK.toString()
                        )
                }
                false
            }
        }

        create_errand_button.setOnClickListener {
            loading.visibility = View.VISIBLE
            viewModel.callCreateOrderAPI(
                et_desc.text.toString(),
                OrderType.ASK.toString()
            )
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
        viewModel =  ViewModelProviders.of(this, viewModelFactory)
                .get(OrdersViewModelImpl::class.java)
    }

    private fun showNewErrandFailed(@StringRes errorString: Int) {
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