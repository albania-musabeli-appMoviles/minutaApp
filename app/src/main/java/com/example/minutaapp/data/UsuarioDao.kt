package com.example.minutaapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


// DAO (Data Access Object) para manejar SQLite con Room Library
// Room es una capa de abstraccion de SQLite
@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun buscarPorCorreo(correo: String): Usuario?
}