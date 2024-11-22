package com.alexnemyr.testtaska.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.handler.MessageType
import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.domain.model.UserDomain
import com.alexnemyr.testtaska.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val mtbUIState: MutableStateFlow<MainScreenState> =
        MutableStateFlow(MainScreenState.defaultState)
    val uiState: StateFlow<MainScreenState> = mtbUIState.asStateFlow()

    private val userPoolFlow: MutableStateFlow<List<UserDomain>> =
        MutableStateFlow(emptyList())

    init {
        fetchUserPool()
    }

    fun onSearch(value: String) {
        viewModelScope.launch {
            val filteredUsers = userPoolFlow.value
                .filter { it.name.contains(value) }
            mtbUIState.emit(
                mtbUIState.value.update(
                    searchInput = value,
                    users = filteredUsers
                )
            )
        }
    }

    fun onQuantity(value: String) {
        viewModelScope.launch {
            Timber.tag(APP_TAG).e("onQuantity userPoolFlow.value.size = ${userPoolFlow.value.size}")
            if (value.isNotBlank()) {
                if (value.toInt() <= userPoolFlow.value.size) {
                    val filteredUsers = userPoolFlow.value
                        .slice(0..<value.toInt())
                    mtbUIState.emit(
                        mtbUIState.value.update(
                            quantityInput = value,
                            users = filteredUsers
                        )
                    )
                }
            } else {
                mtbUIState.emit(
                    mtbUIState.value.update(
                        quantityInput = "",
                        users = userPoolFlow.value
                    )
                )
            }

        }
    }

    private fun fetchUserPool() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.fetchUserList().collect { userPool ->
                when (userPool) {
                    is Result.Success -> {
                        userPoolFlow.emit(userPool.data)
                        mtbUIState.emit(
                            mtbUIState.value
                                .update(
                                    users = userPool.data,
                                    showError = userPool.data.isEmpty()
                                )
                        )
                    }

                    is Result.SuccessWithMessage -> {
                        userPoolFlow.emit(userPool.data)
                        mtbUIState.emit(
                            mtbUIState.value
                                .update(
                                    users = userPool.data,
                                    showError = userPool.message == MessageType.NO_INTERNET_CONNECTION
                                )
                        )
                    }

                    is Result.Error -> {
                        mtbUIState.emit(
                            mtbUIState.value
                                .update(
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
