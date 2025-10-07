package com.example.minutaapp.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object RecetaDbHelper {
    data class OperationResult(
        val ok: Boolean,
        val mensaje: String? = null
    )

    // Crear o actualizar receta
    fun guardarReceta(
        context: Context,
        receta: Receta,
        isEditMode: Boolean,
        callback: (OperationResult) -> Unit
    ) {
        val db = AppDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (isEditMode) {
                    db.recetaDao().actualizar(receta)
                } else {
                    db.recetaDao().insertar(receta)
                }
                withContext(Dispatchers.Main) {
                    callback(OperationResult(true))
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback(OperationResult(false, "Error DB: ${ex.message}"))
                }
            }
        }
    }

    // Eliminar receta
    fun eliminarReceta(
        context: Context,
        receta: Receta,
        callback: (OperationResult) -> Unit
    ) {
        val db = AppDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.recetaDao().eliminar(receta)
                withContext(Dispatchers.Main) {
                    callback(OperationResult(true))
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback(OperationResult(false, "Error DB: ${ex.message}"))
                }
            }
        }
    }

    // Obtener recetas por usuario
    fun obtenerRecetas(
        context: Context,
        correo: String,
        callback: (List<Receta>) -> Unit
    ) {
        val db = AppDatabase.getDatabase(context)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val recetas = db.recetaDao().obtenerRecetasPorUsuario(correo)
                withContext(Dispatchers.Main) {
                    callback(recetas)
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    callback(emptyList())
                }
            }
        }
    }
}