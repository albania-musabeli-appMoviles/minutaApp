package com.example.minutaapp.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    navController: NavHostController,
    receta: Receta,
    onEliminarReceta: (Receta) -> Unit
) {
    // Variable para mostrar toast
    val context = LocalContext.current

    Scaffold(
        topBar = {
            SimpleTopBar(
                title = "Detalle de receta",
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
                .verticalScroll(rememberScrollState()), // esto hace que la column sea desplazable
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card para mostrar detalles de la receta
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceBright
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    // Titulo de la receta
                    Text(
                        text = receta.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    // Tipo de comida
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Tipo de comida",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Tipo: ${receta.tipo}",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    // Ingredientes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Ingredientes",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Ingredientes:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                    // Bucle for each para mostrar cada ingrediente de la receta
                    receta.ingredientes.forEach { ingrediente ->
                        Text(
                            text = "- $ingrediente",
                            fontSize = 16.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, bottom = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // Recomendación nutricional
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = "Recomendación Nutricional",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Recomendación Nutricional:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = receta.recomendacionNutricional,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 24.dp, bottom = 8.dp)
                    )
                }
            }

            /* BOTONES PARA EDITAR/ELIMINAR */
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                // Boton ELIMINAR
                OutlinedButton(
                    onClick = {
                        Toast.makeText(context, "Receta eliminada", Toast.LENGTH_SHORT).show()
                        onEliminarReceta(receta)
                        navController.navigate("minuta"){
                            popUpTo("minuta"){ inclusive = true }
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
                    ){
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Eliminar receta",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text("Eliminar")
                    }
                }

                // Boton EDITAR
                Button(
                    onClick = {
                        // navegar a NewMinutaScreen pasando el ID de la receta
                        navController.navigate("nueva_minuta/${receta.id}?editMode=true")
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
                    ){
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Editar receta",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Text("Editar")
                    }
                }
            }
        }
    }
}