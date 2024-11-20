package com.alexnemyr.testtaska.data.datasource.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import net.sqlcipher.database.SQLiteDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStorageDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserStorageDataSource {

    //todo:for test move to Keystore
    private val dbPassword = "your_secure_password"

    private val passphrase: ByteArray = SQLiteDatabase.getBytes(dbPassword.toCharArray())
    private val factory = SQLCipherHelperFactory(passphrase)
    private val db: AppDatabase = Room.databaseBuilder(
        context = context,
        AppDatabase::class.java,
        USER_DB_NAME
    )
        .openHelperFactory(factory)
        .build()


    override fun getAll(): List<User> =
        db.userDao().getAll()


    override fun loadAllByIds(userIds: IntArray): List<User> =
        db.userDao().loadAllByIds(userIds)

    override fun findByName(name: String): User =
        db.userDao().findByName(name)

    override fun insertAll(users: List<User>) =
        db.userDao().insertAll(users)

    override fun delete(user: User) =
        db.userDao().delete(user)

    companion object {
        const val USER_DB_NAME = "users_database"
    }

}

