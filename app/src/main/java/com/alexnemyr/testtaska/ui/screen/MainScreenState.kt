package com.alexnemyr.testtaska.ui.screen

import com.alexnemyr.testtaska.data.datasource.network.model.responce.UserPoolItem

data class MainScreenState(
    val users: List<UserPoolItem>,
    val searchInput: String,
    val hasInternetConnection: Boolean,
) {
    companion object {
        val defaultState = MainScreenState(
            users = emptyList(),
            searchInput = "",
            hasInternetConnection = false
        )
    }
}
