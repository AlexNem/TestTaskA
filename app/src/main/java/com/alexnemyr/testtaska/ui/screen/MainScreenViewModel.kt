package com.alexnemyr.testtaska.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.Client
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManager
import com.alexnemyr.testtaska.data.datasource.network.model.responce.UserPoolItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val client: Client
) : ViewModel() {

    private val name = this.javaClass.simpleName

    private val hasInternetConnection = MutableStateFlow(false)
    private val userPoolFlow: MutableStateFlow<List<UserPoolItem>> =
        MutableStateFlow(emptyList())

    private val mtbUIState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState.defaultState)
    val uiState: StateFlow<MainScreenState> = mtbUIState.asStateFlow()

    init {
        checkInternetConnection()
        fetchUserPool()
    }

    fun onSearch(value: String) {
        viewModelScope.launch {
            val filteredUsers = userPoolFlow.value
                .filter { it.login.contains(value) }
            mtbUIState.emit(
                mtbUIState.value.copy(
                    searchInput = value,
                    users = filteredUsers
                )
            )
        }
    }

    private fun checkInternetConnection() {
        viewModelScope.launch {
            networkManager.startListenNetworkState()
            val isNetworkConnectedFlow = networkManager.isNetworkConnectedFlow
            isNetworkConnectedFlow.collect {
                hasInternetConnection.emit(it)
                mtbUIState.emit(mtbUIState.value.copy(hasInternetConnection = it))
                Timber.tag(APP_TAG).d(
                    "$name -> checkInternetConnection -> collect -> " +
                            "\nisNetworkConnectedFlow = $it"
                )
            }
        }
    }

    private fun fetchUserPool() {
        viewModelScope.launch {
            val userPool = client.getUserPool()
            Timber.tag(APP_TAG).d(
                "$name -> fetchUserPool" +
                        "\nuserPool = $userPool" +
                        ""
            )
            hasInternetConnection.collect {
                userPoolFlow.emit(userPool)
                if (it) {
                    mtbUIState.emit(mtbUIState.value.copy(users = userPool))
                    Timber.tag(APP_TAG).d(
                        "$name -> fetchUserPool" +
                                "\nuserPool = $userPool" +
                                ""
                    )
                } else {
                    Timber.tag(APP_TAG).d(
                        "$name -> fetchUserPool" +
                                "\nelse -> ${mtbUIState.value.hasInternetConnection}" +
                                ""
                    )
                    //todo: show UI message
                }
            }

        }
    }

}
