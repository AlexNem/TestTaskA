package com.alexnemyr.testtaska.ui.screen

import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.domain.model.UserDomain
import timber.log.Timber

data class MainScreenState(
    val users: List<UserDomain>,
    val searchInput: String,
    val showError: Boolean,
    val errorMessage: String?
) {

    companion object {
        val defaultState = MainScreenState(
            users = emptyList(),
            searchInput = "",
            showError = false,
            errorMessage = null
        )
    }
}

fun MainScreenState.update(
    users: List<UserDomain>? = null,
    searchInput: String? = null,
    showError: Boolean? = null,
    errorMessage: String? = null
): MainScreenState {
    val newState = MainScreenState(
        users ?: this.users,
        searchInput?: this.searchInput,
        showError?: this.showError,
        errorMessage?: this.errorMessage
    )
    Timber.tag(APP_TAG).i("%s", "MainScreenState -> update" +
            "\noldState = $this" +
            "\nnewState = $newState")
    return newState
}
