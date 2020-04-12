package com.cryptox.glovid.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.cryptox.glovid.BaseApplication
import com.cryptox.glovid.network.api.APIService
import com.cryptox.glovid.network.environment.BaseEnvironment
import com.cryptox.glovid.network.environment.Environment
import com.cryptox.glovid.network.http.BaseHttpClient
import com.cryptox.glovid.network.http.HttpClient
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {

    @Provides
    @Singleton
    fun provideApplicationContext(baseApplication: BaseApplication): Context {
        return baseApplication
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: HttpClient): Retrofit {
        return httpClient.getRetrofit()
    }


    @Provides
    @Singleton
    fun provideAPIService(httpClient: HttpClient): APIService {
        return httpClient.getApiService()
    }

    @Provides
    @Singleton
    fun provideHttpClient(environment: Environment): HttpClient {
        return BaseHttpClient(environment)
    }

    @Provides
    @Singleton
    fun provideEnvironment(): Environment {
        return BaseEnvironment()
    }
}