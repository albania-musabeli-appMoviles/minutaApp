package com.example.minutaapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RecetaDao {
    @Insert
    suspend fun insertar(receta: Receta)

    @Update
    suspend fun actualizar(receta: Receta)

    @Delete
    suspend fun eliminar(receta: Receta)

    @Query("SELECT * FROM recetas WHERE usuarioCorreo = :correo")
    suspend fun obtenerRecetasPorUsuario(correo: String): List<Receta>

    @Query("SELECT * FROM recetas WHERE id = :id LIMIT 1")
    suspend fun obtenerRecetaPorId(id: String): Receta?
}