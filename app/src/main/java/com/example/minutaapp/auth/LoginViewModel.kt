package com.example.minutaapp.auth

class LoginViewModel (private val repo: AuthRepository){

    fun login(correo: String, password: String): LoginResult {
        if (correo.isBlank() || password.isBlank() ){
            // mensaje definido para el test
            return LoginResult(false, "Campos vacíos")
        }
        return repo.autenticar(correo, password)
    }
}