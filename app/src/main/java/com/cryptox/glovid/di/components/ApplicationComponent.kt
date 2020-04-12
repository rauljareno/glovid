package com.cryptox.glovid.di.components

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import retrofit2.Retrofit
import com.cryptox.glovid.BaseApplication
import com.cryptox.glovid.di.modules.ActivityModule
import com.cryptox.glovid.di.modules.ApplicationModule
import com.cryptox.glovid.di.modules.RepositoryModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class,ApplicationModule::class, RepositoryModule::class, ActivityModule::class])
interface ApplicationComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun applicationModule(applicationModule: ApplicationModule):Builder
        fun build(): ApplicationComponent
    }

    fun inject(baseApplication: BaseApplication)
    fun getRetrofit():Retrofit
}