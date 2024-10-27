package com.alexnemyr.testtaska.data.datasource.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexnemyr.testtaska.domain.model.UserDomain

@Entity
data class User(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "user_name") val name: String,
    @ColumnInfo(name = "user_url") val url: String?
)

val User.toDomain: UserDomain
    get() = UserDomain(
        id = this.id,
        name = this.name,
        url = this.url
    )