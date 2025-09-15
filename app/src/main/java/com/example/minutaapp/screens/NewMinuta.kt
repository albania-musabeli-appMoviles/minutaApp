package com.example.minutaapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.example.minutaapp.screens.data.Receta

import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMinutaScreen(
    navController: NavHostController, // navegación
    onRecetaAgregada: (Receta) -> Unit, // Función lambda para agregar una nueva receta
    onRecetaEditada: (Receta) -> Unit, // Función lambda para actualizar una receta existente
    recetaToEdit: Receta?, // Objeto Receta a editar
    editMode: Boolean // Indica si el modo está en Creación o Edición
){

    // Variables de estados para el formulario
    var nombreReceta by remember { mutableStateOf(recetaToEdit?.nombre ?: "") }
    var tipoComidaSeleccionada by remember { mutableStateOf(recetaToEdit?.tipo ?: "Desayuno") }
    var ingredienteActual by remember { mutableStateOf("") }
    val listaIngredientes = remember { mutableStateListOf<String>().apply {
        if (recetaToEdit != null) addAll(recetaToEdit.ingredientes)
    } }
    var expanded by remember { mutableStateOf(false) }
    val tiposComida = listOf("Desayuno", "Almuerzo", "Once", "Cena", "Postre")

    // Mostrar mensaje de Toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        // Condición if: Cambia el titulo según el modo
                    if (editMode) "Editar Minuta" else "Nueva Minuta",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            // Campos del formulario
            OutlinedTextField(
                value = nombreReceta,
                onValueChange = { nombreReceta = it },
                label = { Text("Nombre de la receta") },
                modifier = Modifier.fillMaxWidth()
            )

            // Campo select para tipo de comida
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = tipoComidaSeleccionada,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de comida") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // Bucle for each para mostrar los tipos de comida en el menú desplegable
                    tiposComida.forEach { tipoComida ->
                        DropdownMenuItem(
                            text = { Text(tipoComida) },
                            onClick = {
                                tipoComidaSeleccionada = tipoComida
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Fila para añadir ingredientes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = ingredienteActual,
                    onValueChange = { ingredienteActual = it },
                    label = { Text("Añadir ingrediente") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        // Condición if: Verifica si el ingrediente no está vacio
                        if (ingredienteActual.isNotBlank()) {
                            listaIngredientes.add(ingredienteActual)
                            ingredienteActual = ""
                        }
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Añadir")
                }
            }

            // Lista de ingredientes agregados
            // Condición if: Muestra la lista sólo si tiene elementos
            if (listaIngredientes.isNotEmpty()) {
                Text("Ingredientes:", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaIngredientes) { ingrediente ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = "- $ingrediente",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = {
                                    listaIngredientes.remove(ingrediente)
                                    Toast.makeText(context, "Ingrediente $ingrediente eliminado",
                                        Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Delete,
                                    contentDescription = "Eliminar ingrediente"
                                )
                            }
                        }
                    }
                }
            }

            // Botones para Cancelar/Guardar
            Spacer(modifier = Modifier.height(16.dp)) // Spacer: para agregar separación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        navController.navigate("minuta") {
                            popUpTo("minuta") { inclusive = true }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cancelar",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text("Cancelar")
                    }
                }
                Button(
                    onClick = {
                        // Condición if: verifica que el nombre no esté vació
                        if (nombreReceta.isNotBlank()) {
                            val receta = Receta(
                                id = recetaToEdit?.id ?: UUID.randomUUID().toString(),
                                nombre = nombreReceta,
                                ingredientes = listaIngredientes.toList(),
                                tipo = tipoComidaSeleccionada,
                                recomendacionNutricional = recetaToEdit?.recomendacionNutricional ?: "Añadida por el usuario"
                            )
                            // condición if: verifica si el modo es insersión o edición de receta
                            if (editMode) {
                                onRecetaEditada(receta)
                            } else {
                                onRecetaAgregada(receta)
                            }
                            Toast.makeText(
                                context,
                                if (editMode) "Receta actualizada correctamente" else "Receta guardada",
                                Toast.LENGTH_LONG
                            ).show()
                            navController.navigate("minuta") {
                                popUpTo("minuta") { inclusive = true }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Por favor, ingrese un nombre para la receta",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Guardar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        // Texto del botón según el modo
                        Text(if (editMode) "Actualizar" else "Guardar")
                    }
                }
            }
        }
    }
}