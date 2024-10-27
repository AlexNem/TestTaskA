package com.alexnemyr.testtaska.data.datasource.db

interface UserStorageDataSource {

    fun getAll(): List<User>

    fun loadAllByIds(userIds: IntArray): List<User>

    fun findByName(name: String): User

    fun insertAll(users: List<User>)

    fun delete(user: User)
}