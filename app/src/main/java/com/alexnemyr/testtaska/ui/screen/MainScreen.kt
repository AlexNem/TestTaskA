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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.Client
import com.alexnemyr.testtaska.data.datasource.network.model.UserPoolItem
import timber.log.Timber

@Composable
fun MainScreen(
    paddingValues: PaddingValues,

    ) {
    //for test
    val hasInternetConnection = true
    val userPoolState = remember {
        mutableStateOf(emptyList<UserPoolItem>())
    }
    val filteredPoolState = remember {
        mutableStateOf(emptyList<UserPoolItem>())
    }
    val searchInputState = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = true) {
        val userPool = Client().getUserPool()
        userPoolState.value = userPool
        filteredPoolState.value = userPool
        Timber.tag(APP_TAG).d(
            "MainScreen -> LaunchedEffect -> " +
                    "\ngetUserPool = $userPool"
        )

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val horizontalPadding = 16.dp
        if (hasInternetConnection) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = horizontalPadding),
                value = searchInputState.value,
                onValueChange = {
                    searchInputState.value = it
                    Timber.tag(APP_TAG).i("searchInputState = ${searchInputState.value}")
                    //todo:move to vm
                    if (searchInputState.value.isNotBlank()) {
                        filteredPoolState.value = userPoolState.value.filter {
                            it.login.contains(searchInputState.value)
                        }
                    } else {
                        filteredPoolState.value = userPoolState.value
                    }

                })
        }
        LazyColumn(

        ) {
            items(filteredPoolState.value) { user ->
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