package com.cryptox.glovid.di.modules

import com.cryptox.glovid.ui.donation.NewDonationActivity
import com.cryptox.glovid.ui.errand.ErrandCategoryActivity
import com.cryptox.glovid.ui.errand.NewErrandActivity
import com.cryptox.glovid.ui.home.HomeActivity
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
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeHomeActivity(): HomeActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewErrandActivity(): NewErrandActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNewDonationActivity(): NewDonationActivity
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeErrandCategoryActivity(): ErrandCategoryActivity
}