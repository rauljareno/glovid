package com.cryptox.glovid.di.modules

import com.cryptox.glovid.ui.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

import com.cryptox.glovid.ui.main.MainActivity
import com.cryptox.glovid.ui.signup.SignUpActivity

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSignUpActivity(): SignUpActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeLoginActivity(): LoginActivity
}