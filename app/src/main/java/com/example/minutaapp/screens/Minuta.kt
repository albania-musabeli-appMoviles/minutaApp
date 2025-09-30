package com.example.minutaapp.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.minutaapp.screens.data.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinutaScreen(
    navController: NavHostController,
    recetas: List<Receta>,
    selectedColor: String?,
    onColorSelected: (String?) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) } // estado para visibilidad del menu en icono de config
    var showColorMenu by remember { mutableStateOf(false) } // estado para el menu de colores

    // Colores de las tarjetas para escoger (tonos pastel)
    val colorOptions = listOf(
        "Rojo" to Color(0xFFF44336),
        "Azul" to Color(0xFF2196F3),
        "Verde" to Color(0xFF4CAF50),
        "Color dinámico (Palette)" to null
    )

    // Obtener el color dinámico de Palette
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.logo_nutri)
    val palette = Palette.from(bitmap).generate()
    val vibrantColor = palette.vibrantSwatch?.rgb?.let { Color(it) } ?: MaterialTheme.colorScheme.surface

    // Determinar el color de las cards
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Minuta Semanal",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.alignBy(androidx.compose.ui.layout.FirstBaseline)
                )
                IconButton(
                    onClick = { showColorMenu = true },
                    modifier = Modifier.alignBy(androidx.compose.ui.layout.FirstBaseline)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Create,
                        contentDescription = "Color",
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
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "Ver una receta aleatoria",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )
                    RecipeWidget(
                        recetas = recetas,
                        navController = navController,
                        cardColor = cardColor,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Haga click en una minuta para ver el detalle",
                        modifier = Modifier.padding(16.dp)
                    )
                }
                // Bucle que itera sobre la lista de recetas con indices
                itemsIndexed(recetas) { index, recipe ->
                    RecipeDisplayCard(
                        index = index,
                        recipe = recipe,
                        cardColor = cardColor,
                        onClick = { navController.navigate("recipe_detail/${recipe.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun RecipeDisplayCard(index: Int, recipe: Receta, cardColor: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Índice",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "${index + 1}. ${recipe.nombre}", style = MaterialTheme.typography.titleMedium)
            }
            Text(text = "Tipo: ${recipe.tipo}")
            Text(text = "Ingredientes: ${recipe.ingredientes.joinToString(", ")}")
            Text(text = "Recomendación: ${recipe.recomendacionNutricional}")
        }
    }
}