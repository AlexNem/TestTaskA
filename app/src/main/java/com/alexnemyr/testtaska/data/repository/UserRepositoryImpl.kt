package com.alexnemyr.testtaska.data.repository

import com.alexnemyr.testtaska.data.datasource.db.UserStorageDataSource
import com.alexnemyr.testtaska.data.datasource.db.toDomain
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.Client
import com.alexnemyr.testtaska.domain.model.UserDomain
import com.alexnemyr.testtaska.domain.model.toDAO
import com.alexnemyr.testtaska.domain.model.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val client: Client,
    private val userStorageDataSource: UserStorageDataSource
) : UserRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override suspend fun fetchUserList(isConnected: Boolean): Flow<List<UserDomain>> {
        return flow {
            if (isConnected) {
                emit(getRemoteUserList())
            } else {
                emit(getCashedUserList())
            }
        }
    }

    private suspend fun getRemoteUserList(): List<UserDomain> = withContext(Dispatchers.IO) {
        val result = client.getUserPool().map { it.toDomain }
        userStorageDataSource.insertAll(result.map { it.toDAO })
        result
    }

    private suspend fun getCashedUserList(): List<UserDomain> = withContext(Dispatchers.IO) {
        val result = userStorageDataSource.getAll().map { it.toDomain }
        Timber.tag(APP_TAG).d("getCashedUserList -> result = $result")
        result
    }


}