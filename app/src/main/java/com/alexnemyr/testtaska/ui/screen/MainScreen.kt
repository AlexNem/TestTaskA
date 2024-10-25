package com.alexnemyr.testtaska.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.Client
import com.alexnemyr.testtaska.data.datasource.network.model.UserPoolItem
import timber.log.Timber

@Composable
fun MainScreen(
    paddingValues: PaddingValues,

    ) {

    val userPoolState = remember {
        mutableStateOf(emptyList<UserPoolItem>())
    }

    LaunchedEffect(key1 = true) {
        val userPool = Client().getUserPool()
        userPoolState.value = userPool
        Timber.tag(APP_TAG).d(
            "MainScreen -> LaunchedEffect -> " +
                    "\ngetUserPool = $userPool"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        items(userPoolState.value) { user ->
            Text(text = user.login)
        }
    }
}