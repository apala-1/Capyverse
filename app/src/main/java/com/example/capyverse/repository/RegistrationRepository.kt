package com.example.capyverse.repository

interface RegistrationRepository {

    fun signup(email: String, password: String, callback: (Boolean, String, String) -> Unit)
}