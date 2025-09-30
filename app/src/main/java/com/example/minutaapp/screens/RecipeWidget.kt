package com.example.minutaapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.minutaapp.data.Receta

@Composable
fun RecipeWidget(
    recetas: List<Receta>,
    navController: NavHostController,
    cardColor: Color,
    modifier: Modifier = Modifier
) {
    // Estado para la receta seleccionada
    var selectedRecipe by remember { mutableStateOf(recetas.randomOrNull() ?: Receta("0", "Sin recetas", emptyList(), "N/A", "No hay recetas disponibles")) }


    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor)
    ){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(cardColor) // Usar el color de las tarjetas
                .padding(12.dp)
                .clickable {
                    // Navegar a RecipeDetailScreen
                    navController.navigate("recipe_detail/${selectedRecipe.id}")
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Receta del día",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = selectedRecipe.nombre,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Tipo: ${selectedRecipe.tipo}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Ingredientes: ${selectedRecipe.ingredientes.take(3).joinToString(", ")}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(
                onClick = {
                    // Cambiar a una receta aleatoria
                    selectedRecipe = recetas.randomOrNull() ?: selectedRecipe
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Cambiar Receta")
            }
        }
    }

}