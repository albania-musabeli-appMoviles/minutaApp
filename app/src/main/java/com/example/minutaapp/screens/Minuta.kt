package com.example.minutaapp.screens

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import com.example.minutaapp.R
import com.example.minutaapp.data.Receta
import com.example.minutaapp.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinutaScreen(
    navController: NavHostController,
    recetas: List<Receta>,
    usuarioCorreo: String,
    selectedColor: String?,
    onColorSelected: (String?) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) } // Estado para el menú de ajustes
    var showColorMenu by remember { mutableStateOf(false) } // Estado para el menú de colores

    // Opciones de colores
    val colorOptions = listOf(
        "Rosado" to Color(0xFFF06292),
        //"Rojo" to Color(0xFFEF5350),
        "Azul" to Color(0xFF42A5F5),
        "Verde" to Color(0xFF66BB6A),
        "Color dinámico (Palette)" to null
    )

    // Obtener el color dinámico de Palette
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_nutri)
    val palette = Palette.from(bitmap).generate()
    val vibrantColor = palette.vibrantSwatch?.rgb?.let { Color(it) } ?: MaterialTheme.colorScheme.surface

    // Determinar el color de las tarjetas
    val cardColor = selectedColor?.let { colorName ->
        colorOptions.find { it.first == colorName }?.second
    } ?: vibrantColor

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
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cerrar Sesión") },
                            onClick = {
                                // Limpiar SharedPreferences
                                val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                                prefs.edit().remove("correo").apply()
                                // Navegar a la pantalla de login
                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                                showMenu = false
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Minuta Semanal",
                    style = MaterialTheme.typography.headlineMedium
                )
                IconButton(onClick = { showColorMenu = true }) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Seleccionar Color",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                DropdownMenu(
                    expanded = showColorMenu,
                    onDismissRequest = { showColorMenu = false },
                    offset = DpOffset(x = 200.dp, y = 0.dp)
                ) {
                    colorOptions.forEach { (colorName, _) ->
                        DropdownMenuItem(
                            text = { Text(colorName) },
                            onClick = {
                                onColorSelected(colorName)
                                showColorMenu = false
                            }
                        )
                    }
                }
            }

            // Widget de receta destacada
            RecipeWidget(
                recetas = recetas,
                navController = navController,
                cardColor = cardColor,
                usuarioCorreo = usuarioCorreo
            )

            // Lista de todas las recetas
            Text(
                text = "Todas las minutas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (recetas.isEmpty()) {
                Text(
                    text = "No hay recetas, crea una nueva",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(recetas) { receta ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate("recipe_detail/${receta.id}")
                                },
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = cardColor
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = receta.nombre,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Tipo: ${receta.tipo}",
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}