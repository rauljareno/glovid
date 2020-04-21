package com.cryptox.glovid.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import com.cryptox.glovid.di.ViewModelKey
import com.cryptox.glovid.viewModels.ViewModelFactory
import com.cryptox.glovid.viewModels.login.LoginViewModelImpl
import com.cryptox.glovid.viewModels.orders.OrdersViewModelImpl
import com.cryptox.glovid.viewModels.signup.SignUpViewModelImpl

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModelImpl::class)
    abstract fun bindSignUpViewModel(signUpViewModelImpl: SignUpViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModelImpl::class)
    abstract fun bindLoginViewModel(loginViewModelImpl: LoginViewModelImpl): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OrdersViewModelImpl::class)
    abstract fun bindOrdersViewModel(ordersViewModelImpl: OrdersViewModelImpl): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
