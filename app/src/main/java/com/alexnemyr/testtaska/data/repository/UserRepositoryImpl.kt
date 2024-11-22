package com.alexnemyr.testtaska.data.repository

import com.alexnemyr.testtaska.data.datasource.db.UserStorageDataSource
import com.alexnemyr.testtaska.data.datasource.db.toDomain
import com.alexnemyr.testtaska.data.datasource.network.UserApi
import com.alexnemyr.testtaska.data.datasource.network.handler.MessageType
import com.alexnemyr.testtaska.data.datasource.network.handler.NetworkException
import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManager
import com.alexnemyr.testtaska.domain.model.UserDomain
import com.alexnemyr.testtaska.domain.model.toDAO
import com.alexnemyr.testtaska.domain.model.toDomain
import com.alexnemyr.testtaska.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.io.IOException
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userStorageDataSource: UserStorageDataSource,
    private val networkManager: NetworkManager,
) : UserRepository {

    private var isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        checkInternetConnection()
    }

    override fun fetchUserList(): Flow<Result<List<UserDomain>>> {
        return flow {
            isConnected.collect {
                if (isConnected.value) {
                    emit(getRemoteUserList())
                } else {
                    emit(getCashedUserList())
                }
            }
        }
    }

    private suspend fun getRemoteUserList(): Result<List<UserDomain>> = withContext(Dispatchers.IO) {
        try {
            when (val result = userApi.getUserPoolResult()) {
                is Result.Success -> {
                    userStorageDataSource.insertAll(result.data.map { it.toDomain.toDAO })
                    Result.Success(data = result.data.map { it.toDomain })
                }
                //for feature in future
                is Result.SuccessWithMessage -> {
                    userStorageDataSource.insertAll(result.data.map { it.toDomain.toDAO })
                    Result.Success(data = result.data.map { it.toDomain })
                }

                is Result.Error -> {
                    Result.Error(message = result.message)
                }


            }
        } catch (e: NetworkException) {
            Result.Error(message = e.message)
        }

    }

    private suspend fun getCashedUserList(): Result<List<UserDomain>> = withContext(Dispatchers.IO) {
        try {
            val result = userStorageDataSource.getAll().map { it.toDomain }
            Timber.tag(USER_REPOSITORY_TAG).d("getCashedUserList -> try = $result")
           return@withContext Result.SuccessWithMessage(result, MessageType.NO_INTERNET_CONNECTION)
        } catch (e: IOException) {
            Timber.tag(USER_REPOSITORY_TAG).e("getCashedUserList -> catch = $e")
            return@withContext Result.Error(message = e.message)
        }
    }

    private fun checkInternetConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            networkManager.startListenNetworkState()
            networkManager.isNetworkConnectedFlow//.drop(1)
                .collect {
                    Timber.tag(USER_REPOSITORY_TAG).d("checkInternetConnection -> $it")
                    isConnected.emit(it)
                }
        }
    }

    companion object {
        const val USER_REPOSITORY_TAG = "UserRepository"
    }


}
