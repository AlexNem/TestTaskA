package com.alexnemyr.testtaska.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.data.datasource.network.manager.NetworkManager
import com.alexnemyr.testtaska.domain.repository.UserRepository
import com.alexnemyr.testtaska.domain.model.UserDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val networkManager: NetworkManager,
    private val userRepository: UserRepository
) : ViewModel() {

    private val mtbUIState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState.defaultState)
    val uiState: StateFlow<MainScreenState> = mtbUIState.asStateFlow()

    private val hasInternetConnection = MutableStateFlow(false)
    private val userPoolFlow: MutableStateFlow<List<UserDomain>> =
        MutableStateFlow(emptyList())


    init {
        checkInternetConnection()
    }

    fun onSearch(value: String) {
        viewModelScope.launch {
            val filteredUsers = userPoolFlow.value
                .filter { it.name.contains(value) }
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
                fetchUserPool()
            }
        }
    }

    private fun fetchUserPool() {
        viewModelScope.launch {
            userRepository.fetchUserList(hasInternetConnection.value).collect { userPool ->
                when (userPool) {
                    is Result.Success -> {
                        userPoolFlow.emit(userPool.data)
                        mtbUIState.emit(
                            mtbUIState.value
                                .copy(
                                    users = userPool.data,
                                    showError = userPool.data.isEmpty()
                                )
                        )
                    }

                    is Result.Error -> {
                        mtbUIState.emit(
                            mtbUIState.value
                                .copy(
                                    showError = true,
                                    errorMessage = userPool.message
                                )
                        )
                    }
                }

            }

        }
    }

}
