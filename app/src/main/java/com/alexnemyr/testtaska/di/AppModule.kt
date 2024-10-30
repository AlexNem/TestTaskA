package com.alexnemyr.testtaska.di

import android.content.Context
import com.alexnemyr.testtaska.data.datasource.db.UserStorageDataSource
import com.alexnemyr.testtaska.data.datasource.db.UserStorageDataSourceImpl
import com.alexnemyr.testtaska.data.datasource.network.UserApi
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManager
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManagerImpl
import com.alexnemyr.testtaska.data.repository.UserRepositoryImpl
import com.alexnemyr.testtaska.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains("ktor.io")
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }
    }

    @Provides
    @Singleton
    fun provideUserApi(
        @ApplicationContext context: Context,
        client: HttpClient
    ): UserApi {
        return UserApi(context, client)
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

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    abstract fun bindUserStorageDataSource(
        userStorageDataSourceImpl: UserStorageDataSourceImpl
    ): UserStorageDataSource

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}
