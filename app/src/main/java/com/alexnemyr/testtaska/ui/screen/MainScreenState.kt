package com.alexnemyr.testtaska.ui.screen

import com.alexnemyr.testtaska.domain.model.UserDomain

data class MainScreenState(
    val users: List<UserDomain>,
    val searchInput: String,
    val hasInternetConnection: Boolean,
    val showError: Boolean
) {
    companion object {
        val defaultState = MainScreenState(
            users = emptyList(),
            searchInput = "",
            hasInternetConnection = false,
            showError = false
        )
    }
}
