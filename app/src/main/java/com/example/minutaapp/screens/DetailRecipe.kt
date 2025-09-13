package com.example.minutaapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.minutaapp.SimpleTopBar
import com.example.minutaapp.screens.data.Receta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavHostController,
    receta: Receta,
    onEliminarReceta: (Receta) -> Unit
) {
    Scaffold(
        topBar = {
            SimpleTopBar(
                title = receta.nombre,
                showBack = true,
                onBack = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = receta.nombre,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Tipo: ${receta.tipo}",
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Ingredientes:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            receta.ingredientes.forEach { ingrediente ->
                Text(
                    text = "- $ingrediente",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                )
            }
            Text(
                text = "Recomendación Nutricional:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = receta.recomendacionNutricional,
                fontSize = 16.sp
            )

            /* BOTONES PARA EDITAR/ELIMINAR */
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                // Boton ELIMINAR
                OutlinedButton(
                    onClick = { onEliminarReceta(receta) },
                    modifier = Modifier.weight(1f)

                ) {
                    Text("Eliminar")
                }

                // Boton EDITAR
                Button(
                    onClick = {
                        // navegar a NewMinutaScreen pasando el ID de la receta
                        navController.navigate("nueva_minuta/${receta.id}?editMode=true")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Editar")
                }


            }
        }
    }
}