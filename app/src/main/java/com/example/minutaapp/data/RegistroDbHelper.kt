package com.example.minutaapp.data

import android.content.Context
import android.util.Patterns
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// se encargará de hacer operaciones con la bbdd

object RegistroDbHelper {
    data class ValidationResult(
        val ok: Boolean,
        val errores: List<String> = emptyList()
    )

    data class LoginResult(
        val ok: Boolean,
        val mensaje: String? = null,
        val usuario: Usuario? = null
    )

    // validar antes de guardar
    fun validarRegistro(
        context: Context,
        username: String,
        correo: String,
        password: String,
        repeatPassword: String,
        callback: (ValidationResult) -> Unit
    ){
        val errores = mutableListOf<String>()

        if (username.isBlank()) errores += "El nombre es obligatorio."

        if (correo.isBlank()){
            errores += "El correo es obligatorio"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches() ){
            errores += "El correo no tiene un formato válido"
        }

        if (password.isBlank()){
            errores += "La contraseña es obligatoria"
        } else if (password.length < 6){
            errores += "La contraseña debe tener al menos 6 caracteres"
        }

        if (password != repeatPassword){
            errores += "Las contraseñas no coinciden"
        }

        // Validar que el correo sea unico en la base de datos (consulta en Room)
        if (errores.isEmpty()){
            val db = AppDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                // conexion con usuarioDao (consulta)
                val usuarioExiste = db.usuarioDao().buscarPorCorreo(correo)
                if (usuarioExiste != null){
                    callback(ValidationResult(false, listOf("El correo ya está registrado")))
                } else {
                    callback(ValidationResult(true))
                }
            }
        }
        else {
            callback(ValidationResult(false, errores))
        }
    }


    // funcion para guardar registro (solo si pasa las validaciones)
    fun guardarRegistro(
        context: Context,
        username: String,
        correo: String,
        password: String,
        repeatPassword: String,
        callback: (ValidationResult) -> Unit
    ){
        validarRegistro(context, username, correo, password, repeatPassword){ validacion ->
            if (!validacion.ok) {
                callback(validacion)
                return@validarRegistro // lambda con etiqueta para saber que esta validando el registro
            }

            // crea el registro
            val usuario = Usuario(
                username = username,
                correo = correo,
                password = password
            )

            // conectar a la bbdd
            val db = AppDatabase.getDatabase(context)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    db.usuarioDao().insertar(usuario)
                    withContext(Dispatchers.Main){
                        callback(ValidationResult(true))
                    }

                } catch (ex: Exception){
                    ex.printStackTrace()
                    withContext(Dispatchers.Main){
                        callback(ValidationResult(false, listOf("Error DB: ${ ex.message }")))
                    }
                }
            }
        }
    }


    // Autenticar con la base de datos
    fun autenticar(
      context: Context,
      correo: String,
      password: String,
      callback: (LoginResult) -> Unit
    ){
        val correoNorm = correo.trim().lowercase()
        val db = AppDatabase.getDatabase(context)

        CoroutineScope(Dispatchers.IO).launch {
            val usuario = db.usuarioDao().buscarPorCorreo(correoNorm)

            if (usuario == null){
                withContext(Dispatchers.Main) {
                    callback(LoginResult(false, "Correo no existe"))
                }
            }
            else if (usuario.password != password) {
                withContext(Dispatchers.Main){
                    callback(LoginResult(false, "Contraseña incorrecta"))
                }
            }
            else {
                // Guardar sesión en SharedPreferences
                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                prefs.edit()
                    .putBoolean("isLogged", true)
                    .putString("correo", usuario.correo)
                    .putString("nombre", usuario.username)
                    .apply()

                withContext(Dispatchers.Main){
                    callback(LoginResult(true, usuario = usuario))
                }
            }
        }
    }

}