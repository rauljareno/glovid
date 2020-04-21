package com.cryptox.glovid.viewModels.orders

import android.util.Patterns
import androidx.lifecycle.*
import com.cryptox.glovid.R
import com.cryptox.glovid.data.model.Order
import com.cryptox.glovid.data.model.User
import com.cryptox.glovid.data.responseModel.OrdersResponse
import com.cryptox.glovid.data.responseModel.UserResponse
import com.cryptox.glovid.network.api.Resource
import com.cryptox.glovid.network.api.ResourceError
import com.cryptox.glovid.repository.OrderRepository
import com.cryptox.glovid.repository.UserRepository
import javax.inject.Inject

class OrdersViewModelImpl @Inject constructor(private val orderRepository: OrderRepository):
    OrdersViewModel, ViewModel(){

    private val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val isError: MutableLiveData<Boolean> = MutableLiveData()
    private val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    private val error: MutableLiveData<ResourceError> = MutableLiveData()
    private val orderResponse:MutableLiveData<Order> = MutableLiveData()
    private val orderCallObserver: Observer<Resource<Order>> = Observer { t -> processOrderResponse(t) }
    private val ordersResponse:MutableLiveData<List<Order>> = MutableLiveData()
    private val ordersCallObserver: Observer<Resource<List<Order>>> = Observer { t -> processOrdersResponse(t) }

    private val _newErrandForm = MutableLiveData<NewErrandFormState>()
    val newErrandFormState: LiveData<NewErrandFormState> = _newErrandForm

    private val _newDonationForm = MutableLiveData<NewDonationFormState>()
    val newDonationFormState: LiveData<NewDonationFormState> = _newDonationForm

    override fun callCreateOrderAPI(detail: String, type: String) {
        orderRepository.createOrder(detail, type).observeForever { orderCallObserver.onChanged(it) }
    }

    override fun createOrder(): LiveData<Order> {
        return orderResponse
    }

    override fun callSearchOrdersAPI(orderTypeList: List<OrderType>, orderStatusList: List<OrderStatus>)
    {
        val typeList : MutableList<String> = ArrayList()
        orderTypeList.map { typeList.add(it.name) }


        val statusList : MutableList<String> = ArrayList()
        orderStatusList.map { statusList.add(it.name) }

        orderRepository.searchOrders(typeList.joinToString(), statusList.joinToString()).observeForever { ordersCallObserver.onChanged(it)}
    }

    override fun searchOrders(): LiveData<List<Order>> {
        return ordersResponse
    }

    override fun getUserOrderById(): LiveData<Order> {
        TODO("Not yet implemented")
    }

    override fun callGetUserOrderByIdAPI(query: String) {
        TODO("Not yet implemented")
    }

    override fun processOrdersResponse(response: Resource<List<Order>>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                setLoading()
            }
            Resource.Status.SUCCESS -> {
                setSuccess()
                ordersResponse.value = response.data
            }
            Resource.Status.ERROR -> {
                setError()
                error.value = response.resourceError
            }
        }
    }

    override fun processOrderResponse(response: Resource<Order>?){
        when(response?.status){
            Resource.Status.LOADING -> {
                setLoading()
            }
            Resource.Status.SUCCESS -> {
                setSuccess()
                orderResponse.value = response.data
            }
            Resource.Status.ERROR -> {
                setError()
                error.value = response.resourceError
            }
        }
    }

    override fun isLoading(): LiveData<Boolean> {
       return isLoading
    }

    override fun getError(): LiveData<ResourceError> {
        return error
    }

    override fun getUserOrders(): LiveData<List<Order>> {
        TODO("Not yet implemented")
    }

    override fun callGetUserOrdersAPI(query: String) {
        TODO("Not yet implemented")
    }

    fun isError():LiveData<Boolean>{
        return isError
    }

    fun isSuccess():LiveData<Boolean>{
        return isSuccess
    }

    private fun setSuccess(){
        isLoading.value = false
        isSuccess.value = true
        isError.value = false
    }

    private fun setError(){
        isLoading.value = false
        isSuccess.value = false
        isError.value = true
    }

    private fun setLoading(){
        isLoading.value = true
        isSuccess.value = false
        isError.value = false
    }

    public override fun onCleared() {
        super.onCleared()
    }

    fun newErrandDataChanged(desc: String) {
        if (desc.isEmpty()) {
            _newErrandForm.value =
                NewErrandFormState(descError = R.string.invalid_desc)
        } else {
            _newErrandForm.value =
                NewErrandFormState(isDataValid = true)
        }
    }

    fun newDonationDataChanged(desc: String) {
        if (desc.isEmpty()) {
            _newDonationForm.value =
                NewDonationFormState(descError = R.string.invalid_desc)
        } else {
            _newDonationForm.value =
                NewDonationFormState(isDataValid = true)
        }
    }
}