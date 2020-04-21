package com.cryptox.glovid.di.modules

import com.cryptox.glovid.ui.donation.NewDonationFragment
import com.cryptox.glovid.ui.errand.ErrandCategoryFragment
import com.cryptox.glovid.ui.errand.NewErrandFragment
import com.cryptox.glovid.ui.home.HomeFragment
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
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment
    @ContributesAndroidInjector
    abstract fun contributeNewErrandFragment(): NewErrandFragment
    @ContributesAndroidInjector
    abstract fun contributeErrandCategoryFragment(): ErrandCategoryFragment
    @ContributesAndroidInjector
    abstract fun contributeNewDonationFragment(): NewDonationFragment
}