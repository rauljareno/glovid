package com.cryptox.glovid.di.modules

import dagger.Module
import dagger.Provides
import com.cryptox.glovid.network.api.APIService
import com.cryptox.glovid.repository.OrderRepository
import com.cryptox.glovid.repository.OrderRepositoryImpl
import com.cryptox.glovid.repository.UserRepository
import com.cryptox.glovid.repository.UserRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(apiService: APIService): UserRepository {
        return UserRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(apiService: APIService): OrderRepository {
        return OrderRepositoryImpl(apiService)
    }
}