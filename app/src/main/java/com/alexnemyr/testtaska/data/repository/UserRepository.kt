package com.alexnemyr.testtaska.data.repository

interface UserRepository {
   fun fetchUserList(): List<String>
}