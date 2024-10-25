package com.alexnemyr.testtaska.data.datasource.network.manager

import kotlinx.coroutines.flow.StateFlow

interface NetworkManager {
    val isNetworkConnectedFlow: StateFlow<Boolean>

    val isNetworkConnected: Boolean

    fun startListenNetworkState()

    fun stopListenNetworkState()
}
