package com.example.minutaapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewMinutaScreen(navController: NavHostController, onRecetaAgregada: (Receta) -> Unit){

    // Estados para el formulario
    var nombreReceta by remember { mutableStateOf("") }
    var tipoComidaSeleccionada by remember { mutableStateOf("Desayuno") }
    var ingredienteActual by remember { mutableStateOf("") }
    val listaIngredientes = remember { mutableStateListOf<String>() }
    var expanded by remember { mutableStateOf(false) }
    val tiposComida = listOf("Desayuno", "Almuerzo", "Once", "Cena", "Postre")

    // Mostrar mensaje de Toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nueva Minuta",
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
        // Mostrar el Snackbar para mensaje de agregado
        //snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

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

            // Campo para añadir ingredientes
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
            if (listaIngredientes.isNotEmpty()) {
                Text("Ingredientes:", style = MaterialTheme.typography.titleMedium)
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listaIngredientes) { ingrediente ->
                        Text(
                            text = "- $ingrediente",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            // Botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Boton Cancelar
                Button(
                    onClick = {
                        // Redirigir a la lista de minutas sin guardar
                        navController.navigate("minuta"){
                            popUpTo("minuta") { inclusive = true } // limpia la pila hasta MinutaScreen
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                //Boton Guardar
                Button(
                    onClick = {
                        if (nombreReceta.isNotBlank() ) {
                            val newRecipe = Receta(
                                nombre = nombreReceta,
                                ingredientes = listaIngredientes.toList(),
                                tipo = tipoComidaSeleccionada,
                                recomendacionNutricional = "Añadida por el usuario"
                            )
                            onRecetaAgregada(newRecipe)

                            // Mensaje de Toast
                            Toast.makeText(
                                context,
                                "Receta guardada correctamente",
                                Toast.LENGTH_LONG
                            ).show()

                            // redirigir a la lista de recetas
                            navController.navigate("minuta"){
                                popUpTo("minuta") { inclusive = true } // Limpia la pila hasta Minuta Screen
                            }
                        } else {
                    // Muestra un mensaje si el nombre está vacío
                    Toast.makeText(
                        context,
                        "Por favor, ingrese un nombre para la receta",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier.weight(1f)
            ) {
            Text("Guardar")
                }
            }

        }
    }

}