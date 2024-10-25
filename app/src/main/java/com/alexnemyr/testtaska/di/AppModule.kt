package com.alexnemyr.testtaska.di

import android.content.Context
import com.alexnemyr.testtaska.data.datasource.network.Client
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManager
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideClient(@ApplicationContext context: Context): Client {
        return Client(context)
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun bindNetworkManager(
        networkManagerImpl: NetworkManagerImpl
    ): NetworkManager

}
