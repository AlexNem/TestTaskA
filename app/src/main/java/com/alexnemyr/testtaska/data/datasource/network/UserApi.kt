package com.alexnemyr.testtaska.data.datasource.network

import android.content.Context
import com.alexnemyr.testtaska.R
import com.alexnemyr.testtaska.data.datasource.network.handler.Result
import com.alexnemyr.testtaska.data.datasource.network.handler.toResult
import com.alexnemyr.testtaska.data.datasource.network.model.responce.UserPoolItem
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class UserApi @Inject constructor(
    private val context: Context,
    private val client: HttpClient
) {

    //for test
    suspend fun getMockPool(): List<UserPoolItem> {
        val fileContent = context.resources.openRawResource(R.raw.users)
            .bufferedReader()
            .use { it.readText() }
        val result = Json.decodeFromString<List<UserPoolItem>>(fileContent)
        Timber.tag(APP_TAG).d("UserApi -> getUserPool -> " +
                "\nresult = $result")
        return result
    }

    suspend fun getUserPoolResult(): Result<List<UserPoolItem>> {
        val result = client.get(BASE_URL + GET_USER_POOL)
            .toResult<List<UserPoolItem>>()
        Timber.tag(APP_TAG).d(
            "UserApi -> getUserPoolResult -> " +
                    "\nresult = $result"
        )
        return result
    }

}
