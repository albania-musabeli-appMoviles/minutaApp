package com.example.minutaapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val correo: String,
    val password: String
)