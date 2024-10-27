package com.alexnemyr.testtaska.domain.model

import com.alexnemyr.testtaska.data.datasource.db.User
import com.alexnemyr.testtaska.data.datasource.network.model.responce.UserPoolItem

data class UserDomain(
    val id: Int,
    val name: String,
    val url: String?
) {
    companion object {
        val defaultUserDomain: UserDomain
            get() = UserDomain(
                id = 0,
                name = "login",
                url = null
            )
    }
}

val UserPoolItem.toDomain: UserDomain
    get() = UserDomain(
        id = this.id,
        name = this.login,
        url = this.avatarUrl
    )

val UserDomain.toDAO: User
    get() = User(
        id = this.id,
        name = this.name,
        url = this.url
    )
