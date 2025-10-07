package com.example.minutaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.util.UUID

@Entity(tableName = "recetas")
data class Receta(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val ingredientes: List<String>,
    val tipo: String,
    val recomendacionNutricional: String,
    val usuarioCorreo: String // Para asociar la receta con el usuario que la creó
) : Serializable

// TypeConverter para manejar List<String>
class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}