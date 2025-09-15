package com.example.minutaapp.screens.data

import java.io.Serializable
import java.util.UUID

// Clase que representa una receta
data class Receta(
    val id: String = UUID.randomUUID().toString(), // ID unico generado automaticamente
    val nombre: String,
    val ingredientes: List<String>,
    val tipo: String,
    val recomendacionNutricional: String
) : Serializable

// para que la navegacion funcione correctamente, la clase Receta debe ser serializable
// Esto para que pueda ser pasable como argumento en la navegacion