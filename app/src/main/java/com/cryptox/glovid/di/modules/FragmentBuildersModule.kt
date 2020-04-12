package com.cryptox.glovid.di.modules

import com.cryptox.glovid.ui.login.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import com.cryptox.glovid.ui.signup.SignUpFragment

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment
}