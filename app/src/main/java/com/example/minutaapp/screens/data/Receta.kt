package com.example.minutaapp.screens.data

data class Receta(
    val nombre: String,
    val ingredientes: List<String>,
    val tipo: String,
    val recomendacionNutricional: String
)