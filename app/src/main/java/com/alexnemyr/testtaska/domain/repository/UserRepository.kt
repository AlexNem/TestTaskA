package com.alexnemyr.testtaska.domain.repository

import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun fetchUserList(): Flow<Result<List<UserDomain>>>
}
