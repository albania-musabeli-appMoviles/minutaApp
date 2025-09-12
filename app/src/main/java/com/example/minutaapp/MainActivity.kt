package com.example.minutaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.minutaapp.ui.theme.MinutaAppTheme

import androidx.navigation.compose.composable

import com.example.minutaapp.screens.ForgotPasswordScreen
import com.example.minutaapp.screens.MinutaScreen
import com.example.minutaapp.screens.NewMinutaScreen
import com.example.minutaapp.screens.RecipeDetailScreen
import com.example.minutaapp.screens.RegisterScreen
import com.example.minutaapp.screens.data.Receta

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinutaAppTheme {
                AppNav()

            }
        }
    }
}

// Navegacion y rutas
@Composable
fun AppNav(){
    val navController = rememberNavController()
    val recetasUsuario = remember { mutableStateListOf<Receta>() }

    // Lista estática de recetas (copiada de MinutaScreen para acceso en RecipeDetailScreen)
    val recipes = listOf(
        Receta(
            nombre = "Ensalada Mediterránea",
            ingredientes = listOf("Lechuga", "Tomate", "Pepino", "Aceitunas", "Queso Feta"),
            tipo = "Almuerzo",
            recomendacionNutricional = "Rica en fibra y antioxidantes, ideal para una dieta equilibrada."
        ),
        Receta(
            nombre = "Avena con Frutas",
            ingredientes = listOf("Avena", "Leche", "Plátano", "Fresas", "Miel"),
            tipo = "Desayuno",
            recomendacionNutricional = "Alta en carbohidratos complejos y vitaminas, perfecta para empezar el día."
        ),
        Receta(
            nombre = "Pollo a la Parrilla con Verduras",
            ingredientes = listOf("Pechuga de pollo", "Brócoli", "Zanahoria", "Pimientos"),
            tipo = "Cena",
            recomendacionNutricional = "Alta en proteínas y baja en grasas, apoya la recuperación muscular."
        ),
        Receta(
            nombre = "Batido Verde",
            ingredientes = listOf("Espinaca", "Manzana", "Apio", "Jengibre", "Agua"),
            tipo = "Snack",
            recomendacionNutricional = "Desintoxicante y bajo en calorías, ideal para un refrigerio saludable."
        ),
        Receta(
            nombre = "Tacos de Pescado",
            ingredientes = listOf("Pescado blanco", "Tortillas de maíz", "Aguacate", "Col", "Salsa"),
            tipo = "Almuerzo",
            recomendacionNutricional = "Fuente de omega-3 y proteínas, promueve la salud cardiovascular."
        )
    )

    // Combinar listas de recetas
    val listaTotalRecetas = recipes + recetasUsuario


    // funcion para eliminar una receta
    fun eliminarReceta(receta: Receta){
        recetasUsuario.remove(receta)
    }


    // CONTROLAR LA NAVEGACION (rutas)
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN){
            LoginScreen (
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoForgot = { navController.navigate(Routes.FORGOT) },
                onLoginSuccess = { navController.navigate(Routes.MINUTA) },
            )
        }
        composable(Routes.REGISTER){
            RegisterScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.FORGOT){
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MINUTA) {
            MinutaScreen(navController = navController, recetas = listaTotalRecetas)
        }
        composable(Routes.NEWMINUTA) {
            NewMinutaScreen(
                navController = navController,
                onRecetaAgregada = { nuevaReceta ->
                    recetasUsuario.add(nuevaReceta)
                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = true } }
                }
            )
        }
        composable(Routes.RECIPE_DETAIL) { backStackEntry ->
            val recipeIndex = backStackEntry.arguments?.getString("recipeIndex")?.toIntOrNull()
            if (recipeIndex != null && recipeIndex in listaTotalRecetas.indices) {
                RecipeDetailScreen(
                    navController = navController,
                    receta = listaTotalRecetas[recipeIndex],
                    onEliminarReceta = { receta ->
                        eliminarReceta(receta)
                        navController.popBackStack() // volver a MinutaScreen
                    }
                )
            } else {
                // Manejo de error: índice inválido
                Text("Error: Receta no encontrada")
            }
        }
    }
}


// Crear un TopBar reutilizable para las paginas RegisterScreen y ForgotPasswordScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    title: String,
    showBack: Boolean,
    onBack: (() -> Unit)? = null
){
    TopAppBar(
        title = {
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            if (showBack){
                IconButton(onClick = {onBack?.invoke() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}