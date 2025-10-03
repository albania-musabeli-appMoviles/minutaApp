package com.example.minutaapp.auth

data class LoginResult(
    val ok: Boolean,
    val mensaje: String? = null,
    val usuario: Usuario? = null
)

data class Usuario(val correo: String, val password: String)

interface AuthRepository {
    fun autenticar(correo: String, password: String): LoginResult
}