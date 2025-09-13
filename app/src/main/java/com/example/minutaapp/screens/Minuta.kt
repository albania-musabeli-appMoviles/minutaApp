package com.example.minutaapp.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.minutaapp.screens.data.Receta

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinutaScreen(navController: NavHostController, recetas: List<Receta>) {

    // Estado para la visibilidad del menú en icono de configuración
    var showMenu by remember { mutableStateOf(false) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Minuta Nutricional",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "Ajustes"
                        )
                    }
                    // Menu desplegable para icono de configuracion
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión") },
                            onClick = {
                                showMenu = false
                                navController.navigate("login")
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("nueva_minuta") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Minuta")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Minuta Semanal",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Haga click en una minuta para ver el detalle",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(recetas) { index, recipe ->
                    RecipeDisplayCard(
                        index = index,
                        recipe = recipe,
                        onClick = { navController.navigate("recipe_detail/${recipe.id}") }
                    )
                }
            }
        }
    }
}


@Composable
fun RecipeDisplayCard(index: Int, recipe: Receta, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "${index + 1}. ${recipe.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Tipo: ${recipe.tipo}")
            Text(text = "Ingredientes: ${recipe.ingredientes.joinToString(", ")}")
            Text(text = "Recomendación: ${recipe.recomendacionNutricional}")
        }
    }
}