package com.alexnemyr.testtaska

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.alexnemyr.testtaska.data.datasource.network.APP_TAG
import com.alexnemyr.testtaska.data.datasource.network.NetworkManager
import com.alexnemyr.testtaska.ui.screen.MainScreen
import com.alexnemyr.testtaska.ui.theme.TestTaskATheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    val viewModel: MainScreenViewModel =
//    @Inject lateinit var client: Client
    @Inject lateinit var networkManager: NetworkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val isNetworkConnected = networkManager.isNetworkConnected
        val isNetworkConnectedFlow = networkManager.isNetworkConnectedFlow
        lifecycleScope.launch {
            isNetworkConnectedFlow.collect {
                Timber.tag(APP_TAG).d("MainActivity -> onCreate -> collect -> " +
                        "\nisNetworkConnectedFlow = $it")
            }
        }
        networkManager.startListenNetworkState()
        Timber.tag(APP_TAG).d("MainActivity -> onCreate -> " +
                "\nisNetworkConnected = $isNetworkConnected")
        setContent {
            TestTaskATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(paddingValues = innerPadding)
                }
            }
        }
    }
}
