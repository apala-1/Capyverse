package com.example.capyverse.repository

interface LoginRepository {

    fun login(email: String, password: String, callback: (Boolean, String) -> Unit)
}