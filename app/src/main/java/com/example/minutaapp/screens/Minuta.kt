package com.example.minutaapp.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.minutaapp.SimpleTopBar
import com.example.minutaapp.data.Receta
import com.example.minutaapp.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinutaScreen(
    navController: NavHostController,
    recetas: List<Receta>,
    usuarioCorreo: String,
    selectedColor: String?,
    onColorSelected: (String) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Minutas",
                showBack = false
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("nueva_minuta") }, // Corregido: usa "nueva_minuta"
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Añadir receta")
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
            // Widget de receta destacada
            RecipeWidget(
                recetas = recetas,
                navController = navController,
                cardColor = selectedColor?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color(0xFFE0F7FA),
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
                                containerColor = selectedColor?.let { Color(android.graphics.Color.parseColor(it)) } ?: Color.White
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
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "Tipo: ${receta.tipo}",
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Botón de cerrar sesión
            Button(
                onClick = {
                    // Limpiar SharedPreferences
                    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().remove("correo").apply()
                    // Navegar a la pantalla de login
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Cerrar Sesión")
            }
        }
    }
}