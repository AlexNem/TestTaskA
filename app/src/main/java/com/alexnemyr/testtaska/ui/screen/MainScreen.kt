package com.alexnemyr.testtaska.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

val horizontalPadding = 16.dp

@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    viewModel: MainScreenViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        if (state.value.hasInternetConnection) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                value = state.value.searchInput,
                onValueChange = {
                    viewModel.onSearch(it)
                })
        }
        LazyColumn() {
            items(state.value.users) { user ->
                Text(
                    modifier = Modifier
                        .padding(vertical = 16.dp),
                    text = user.login,
                    fontSize = 24.sp
                )
            }
        }
    }

}