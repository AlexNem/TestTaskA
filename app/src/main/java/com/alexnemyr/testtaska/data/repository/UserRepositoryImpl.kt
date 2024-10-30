package com.alexnemyr.testtaska.data.repository

import com.alexnemyr.testtaska.data.datasource.db.UserStorageDataSource
import com.alexnemyr.testtaska.data.datasource.db.toDomain
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.UserApi
import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.domain.model.UserDomain
import com.alexnemyr.testtaska.domain.model.toDomain
import com.alexnemyr.testtaska.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
    private val userStorageDataSource: UserStorageDataSource
) : UserRepository {

    override suspend fun fetchUserList(isConnected: Boolean): Flow<Result<List<UserDomain>>> {
        return flow {
            if (isConnected) {
                emit(getRemoteUserList())
            } else {
                emit(
                    Result.Success(getCashedUserList())
                )
            }
        }
    }

    private suspend fun getRemoteUserList(): Result<List<UserDomain>> = withContext(Dispatchers.IO) {
        when (val result = userApi.getUserPoolResult()) {
            is Result.Success -> {
                Result.Success(data = result.data.map { it.toDomain })
            }

            is Result.Error -> {
                Result.Error(message = "error")
            }
        }
    }

    private suspend fun getCashedUserList(): List<UserDomain> = withContext(Dispatchers.IO) {
        val result = userStorageDataSource.getAll().map { it.toDomain }
        Timber.tag(APP_TAG).d("getCashedUserList -> result = $result")
        result
    }


}