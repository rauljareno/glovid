package com.cryptox.glovid

import android.app.Activity
import android.app.Application
import android.app.Service
import dagger.android.HasActivityInjector
import com.cryptox.glovid.di.AppInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasServiceInjector
import com.cryptox.glovid.di.components.ApplicationComponent
import com.cryptox.glovid.utils.Prefs
import javax.inject.Inject

val prefs: Prefs by lazy {
    BaseApplication.prefs!!
}

class BaseApplication : Application(), HasActivityInjector , HasServiceInjector{

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
            singleton = this
            prefs = Prefs(applicationContext)
            AppInjector.init(this)
    }

    override fun activityInjector() = dispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> {
        return dispatchingServiceInjector
    }

    companion object {
        // Singleton Instance
        private lateinit var singleton: BaseApplication

        fun getInstance() : BaseApplication { return singleton }

        var prefs: Prefs? = null

    }

}