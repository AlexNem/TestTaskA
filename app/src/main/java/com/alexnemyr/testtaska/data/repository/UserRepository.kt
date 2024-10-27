package com.alexnemyr.testtaska.data.repository

import com.alexnemyr.testtaska.domain.model.UserDomain
import kotlinx.coroutines.flow.Flow

interface UserRepository {
   suspend fun fetchUserList(isConnected: Boolean): Flow<List<UserDomain>>
}