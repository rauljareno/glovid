package com.cryptox.glovid.ui.donation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.CategoryEnum
import com.cryptox.glovid.data.model.SubcategoryEnum
import com.cryptox.glovid.databinding.FragmentNewDonationBinding
import com.cryptox.glovid.di.Injectable
import com.cryptox.glovid.ui.home.HomeFragment
import com.cryptox.glovid.viewModels.orders.OrderType
import com.cryptox.glovid.viewModels.orders.OrdersViewModelImpl
import kotlinx.android.synthetic.main.fragment_new_donation.*
import javax.inject.Inject


open class NewDonationFragment(categoryEnum: CategoryEnum?, subcategoryEnum: SubcategoryEnum?) : Fragment(), Injectable {

    private val category = categoryEnum
    private val subcategory = subcategoryEnum

    private val TAG = NewDonationFragment::class.java.simpleName

    private lateinit var viewModel: OrdersViewModelImpl

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var dataBinding: FragmentNewDonationBinding

    private lateinit var selectedImage: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dataBinding =  DataBindingUtil.inflate(inflater ,
            R.layout.fragment_new_donation,container , false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setViewModel()
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this

        viewModel.newDonationFormState.observe(viewLifecycleOwner, Observer {
            val newDonationState = it ?: return@Observer

            // disable new errand button unless description is valid
            create_donation_button.isEnabled = newDonationState.isDataValid

            if (newDonationState.descError != null) {
                et_desc.error = getString(newDonationState.descError)
            }
        })

        viewModel.createOrder().observe(viewLifecycleOwner, Observer {
            val order = it ?: return@Observer
            loading.visibility = View.GONE
            val intent = Intent(activity!!, DonationCreatedActivity::class.java)
            startActivity(intent)
            HomeFragment.needRefresh = true
            activity!!.setResult(Activity.RESULT_OK)
            activity!!.finish()
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            val error = it ?: return@Observer
            loading.visibility = View.GONE
            showNewDonationFailed(R.string.new_donation_failed)//loginResult.error)
        })

        et_desc.apply {
            afterTextChanged {
                viewModel.newDonationDataChanged(
                    et_desc.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        loading.visibility = View.VISIBLE
                        val desc = et_desc.text.toString()
                        if (desc.isEmpty()) {
                            viewModel.callCreateOrderAPI(
                                getDonationDescription(),
                                OrderType.GIVE.toString()
                            )
                        } else {
                            viewModel.callCreateOrderAPI(
                                getDonationDescription() + " : " + desc,
                                OrderType.GIVE.toString()
                            )
                        }
                    }
                }
                false
            }
        }

        create_donation_button.setOnClickListener {
            loading.visibility = View.VISIBLE
            val desc = et_desc.text.toString()
            if (desc.isEmpty()) {
                viewModel.callCreateOrderAPI(
                    getDonationDescription(),
                    OrderType.GIVE.toString()
                )
            } else {
                viewModel.callCreateOrderAPI(
                    getDonationDescription() + " : " + desc,
                    OrderType.GIVE.toString()
                )
            }
        }

        //BUTTON CLICK
        /*add_image_1.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        add_image_2.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        add_image_3.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_3
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        add_image_4.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_4
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        add_image_5.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_5
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        //BUTTON CLICK
        add_image_6.setOnClickListener {
            //check runtime permission
            selectedImage = add_image_6
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }*/
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

    private fun showNewDonationFailed(@StringRes errorString: Int) {
        Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show()
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                    //permission from popup granted
                    pickImageFromGallery()
                }
                else{
                    //permission from popup denied
                    Toast.makeText(context!!, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            selectedImage.setImageURI(data?.data)
        }
    }

    private fun getDonationDescription(): String {
        return when (category) {
            CategoryEnum.CATEGORY_BUY -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_BUY_FOOD -> {
                        "comprar comida"
                    }
                    SubcategoryEnum.SUBCATEGORY_BUY_PHARMACY -> {
                        "comprar medicación"
                    }
                    SubcategoryEnum.SUBCATEGORY_BUY_TOBACCO -> {
                        "comprar tabaco"
                    }
                    SubcategoryEnum.SUBCATEGORY_BUY_KIOSK -> {
                        "comprar revistas"
                    }
                    else -> {
                        "comprar"
                    }
                }
            }
            CategoryEnum.CATEGORY_FUN -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_FUN_CHAT -> {
                        "conversar"
                    }
                    SubcategoryEnum.SUBCATEGORY_FUN_MOVIE -> {
                        "recomendaciones de películas"
                    }
                    SubcategoryEnum.SUBCATEGORY_FUN_SING -> {
                        "cantar"
                    }
                    SubcategoryEnum.SUBCATEGORY_FUN_DANCE -> {
                        "bailar"
                    }
                    SubcategoryEnum.SUBCATEGORY_FUN_PLAY -> {
                        "jugar"
                    }
                    else -> {
                        "diversión"
                    }
                }
            }
            CategoryEnum.CATEGORY_MAIL -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_MAIL_SEND_PACKAGE -> {
                        "enviar un paquete"
                    }
                    SubcategoryEnum.SUBCATEGORY_MAIL_RECEIVE_PACKAGE -> {
                        "recibir un paquete"
                    }
                    SubcategoryEnum.SUBCATEGORY_MAIL_SEND_MONEY -> {
                        "enviar dinero"
                    }
                    SubcategoryEnum.SUBCATEGORY_MAIL_RECEIVE_MONEY -> {
                        "recibir dinero"
                    }
                    SubcategoryEnum.SUBCATEGORY_MAIL_FAX_BUROFAX -> {
                        "enviar un fax/burofax"
                    }
                    else -> {
                        "servicio de mensajería"
                    }
                }
            }
            CategoryEnum.CATEGORY_TRANSPORT -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_TRANSPORT_HOSPITAL -> {
                        "ir al hospital"
                    }
                    SubcategoryEnum.SUBCATEGORY_TRANSPORT_AIRPORT -> {
                        "ir al aeropuerto"
                    }
                    SubcategoryEnum.SUBCATEGORY_TRANSPORT_STATION -> {
                        "ir a la estación"
                    }
                    SubcategoryEnum.SUBCATEGORY_TRANSPORT_WORK -> {
                        "ir al trabajo"
                    }
                    SubcategoryEnum.SUBCATEGORY_TRANSPORT_SHOPPING -> {
                        "ir a comprar"
                    }
                    else -> {
                        "un medio de transporte"
                    }
                }
            }
            CategoryEnum.CATEGORY_HOMEWORK -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_MATH -> {
                        "ayuda con matemáticas"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_LANGUAGE -> {
                        "ayuda con idiomas"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_BIOLOGY -> {
                        "ayuda con biología"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_PHYSICS -> {
                        "ayuda con física"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_CHEMISTRY -> {
                        "ayuda con química"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOMEWORK_HISTORY -> {
                        "ayuda con historia"
                    }
                    else -> {
                        "ayuda con los deberes"
                    }
                }
            }
            CategoryEnum.CATEGORY_ITEMS -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_ITEMS_TOOLS -> {
                        "herramientas"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_KITCHEN -> {
                        "utensilios de cocina"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_BATHROOM -> {
                        "artículos de baño"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_CLOTHES -> {
                        "ropa"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_CLEANING -> {
                        "artículos de limpieza"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_TOYS -> {
                        "juguetes"
                    }
                    SubcategoryEnum.SUBCATEGORY_ITEMS_FOOD -> {
                        "comida"
                    }
                    else -> {
                        "artículos"
                    }
                }
            }
            CategoryEnum.CATEGORY_CARE -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_CARE_CHILDREN -> {
                        "cuidado de niños"
                    }
                    SubcategoryEnum.SUBCATEGORY_CARE_ELDERS -> {
                        "cuidado de ancianos"
                    }
                    SubcategoryEnum.SUBCATEGORY_CARE_PETS -> {
                        "cuidado de mascotas"
                    }
                    else -> {
                        "cuidado"
                    }
                }
            }
            CategoryEnum.CATEGORY_SERVICES -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_SERVICES_DOCTOR -> {
                        "gestionar una cita con el médico"
                    }
                    SubcategoryEnum.SUBCATEGORY_SERVICES_PHONE -> {
                        "gestionar un servicio de telefonía"
                    }
                    SubcategoryEnum.SUBCATEGORY_SERVICES_INTERNET -> {
                        "gestionar un servicio de internet"
                    }
                    SubcategoryEnum.SUBCATEGORY_SERVICES_ELECTRICITY -> {
                        "gestionar un servicio de luz"
                    }
                    SubcategoryEnum.SUBCATEGORY_SERVICES_GAS -> {
                        "gestionar un servicio de gas"
                    }
                    else -> {
                        "gestionar un servicio"
                    }
                }
            }
            CategoryEnum.CATEGORY_PET_WALKING -> {
                "ayuda paseando mascota"
            }
            CategoryEnum.CATEGORY_PSYCHOLOGICAL_AID -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_DEPRESSION -> {
                        "ayuda para la depresión"
                    }
                    SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_ANXIETY -> {
                        "ayuda para la ansiedad"
                    }
                    SubcategoryEnum.SUBCATEGORY_PSYCHOLOGICAL_AID_ABUSE -> {
                        "ayuda para el maltrato"
                    }
                    else -> {
                        "ayuda psicológica"
                    }
                }
            }
            CategoryEnum.CATEGORY_FIRST_AID -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_FIRST_AID_BURN -> {
                        "primeros auxilios por una quemadura"
                    }
                    SubcategoryEnum.SUBCATEGORY_FIRST_AID_CUT -> {
                        "primeros auxilios por un corte"
                    }
                    SubcategoryEnum.SUBCATEGORY_FIRST_AID_BLEEDING -> {
                        "primeros auxilios por una hemorragia"
                    }
                    SubcategoryEnum.SUBCATEGORY_FIRST_AID_SUFFOCATION -> {
                        "primeros auxilios por una asfixia"
                    }
                    SubcategoryEnum.SUBCATEGORY_FIRST_AID_FAINT -> {
                        "primeros auxilios por un desmayo"
                    }
                    else -> {
                        "primeros auxilios"
                    }
                }
            }
            CategoryEnum.CATEGORY_HOUSEWORK -> {
                when (subcategory) {
                    SubcategoryEnum.SUBCATEGORY_HOUSEWORK_FIXING -> {
                        "ayuda para arreglar desperfectos en el hogar"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOUSEWORK_CLEANING -> {
                        "ayuda para limpiar el hogar"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOUSEWORK_WASHING_CLOTHES -> {
                        "ayuda para limpiar su ropa"
                    }
                    SubcategoryEnum.SUBCATEGORY_HOUSEWORK_TRASH -> {
                        "ayuda para sacar la basura"
                    }
                    else -> {
                        "ayuda en el hogar"
                    }
                }
            }
            CategoryEnum.CATEGORY_OTHERS -> {
                "ayuda"
            }
            else -> ""
        }
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