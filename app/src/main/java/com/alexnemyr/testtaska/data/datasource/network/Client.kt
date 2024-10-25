package com.alexnemyr.testtaska.data.datasource.network

import com.alexnemyr.testtaska.data.datasource.network.model.UserPoolItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject


class Client @Inject constructor() {
    val cioClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
            filter { request ->
                request.url.host.contains("ktor.io")
            }
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }
    }

    suspend fun getUserPool(): List<UserPoolItem> {
        val result = cioClient.get(BASE_URL + GET_USER_POOL).body<List<UserPoolItem>>()
        Timber.tag(APP_TAG).d(
            "Client -> getUserPool -> " +
                    "\nresult = $result"
        )
        return result
    }

}
