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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.minutaapp.ui.theme.MinutaAppTheme

import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.minutaapp.screens.ForgotPasswordScreen
import com.example.minutaapp.screens.MinutaScreen
import com.example.minutaapp.screens.NewMinutaScreen
import com.example.minutaapp.screens.RecipeDetailScreen
import com.example.minutaapp.screens.RegisterScreen
import com.example.minutaapp.data.Receta

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
    val recetasEstaticasEditadas = remember { mutableStateListOf<Receta>() }
    var selectedColor by rememberSaveable { mutableStateOf<String?>(null) }

    // Lista estática de recetas (copiada de MinutaScreen para acceso en RecipeDetailScreen)
    val recipes = listOf(
        Receta(
            id = "1",
            nombre = "Ensalada Mediterránea",
            ingredientes = listOf("Lechuga", "Tomate", "Pepino", "Aceitunas", "Queso Feta"),
            tipo = "Almuerzo",
            recomendacionNutricional = "Rica en fibra y antioxidantes, ideal para una dieta equilibrada."
        ),
        Receta(
            id = "2",
            nombre = "Avena con Frutas",
            ingredientes = listOf("Avena", "Leche", "Plátano", "Fresas", "Miel"),
            tipo = "Desayuno",
            recomendacionNutricional = "Alta en carbohidratos complejos y vitaminas, perfecta para empezar el día."
        ),
        Receta(
            id = "3",
            nombre = "Pollo a la Parrilla con Verduras",
            ingredientes = listOf("Pechuga de pollo", "Brócoli", "Zanahoria", "Pimientos"),
            tipo = "Cena",
            recomendacionNutricional = "Alta en proteínas y baja en grasas, apoya la recuperación muscular."
        ),
        Receta(
            id = "4",
            nombre = "Batido Verde",
            ingredientes = listOf("Espinaca", "Manzana", "Apio", "Jengibre", "Agua"),
            tipo = "Snack",
            recomendacionNutricional = "Desintoxicante y bajo en calorías, ideal para un refrigerio saludable."
        ),
        Receta(
            id = "5",
            nombre = "Tacos de Pescado",
            ingredientes = listOf("Pescado blanco", "Tortillas de maíz", "Aguacate", "Col", "Salsa"),
            tipo = "Almuerzo",
            recomendacionNutricional = "Fuente de omega-3 y proteínas, promueve la salud cardiovascular."
        )
    )

    // Combinar listas de recetas, priorizando las editadas
    val listaTotalRecetas = recetasEstaticasEditadas + recipes.filter { staticRecipe ->
        recetasEstaticasEditadas.none {it.id == staticRecipe.id}
    } + recetasUsuario


    // funcion para eliminar una receta
    fun eliminarReceta(receta: Receta){
        recetasUsuario.remove(receta) // eliminar recetas del usuario
        recetasEstaticasEditadas.removeAll {it.id == receta.id} // eliminar receta estatica
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
            ForgotPasswordScreen(onBack = { navController.popBackStack() }, navController = navController)
        }
        composable(Routes.MINUTA) {
            MinutaScreen(
                navController = navController,
                recetas = listaTotalRecetas,
                selectedColor = selectedColor,
                onColorSelected = { newColor -> selectedColor = newColor }
            )
        }
        composable(Routes.NEWMINUTA) {
            NewMinutaScreen(
                navController = navController,
                onRecetaAgregada = { nuevaReceta ->
                    recetasUsuario.add(nuevaReceta)
                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                },
                onRecetaEditada = { recetaEditada ->
                    // esta funcion no se usa en modo creación, por eso está vacía
                },
                recetaToEdit = null, // no hay receta para editar en modo crear receta
                editMode = false // no está en modo editar
            )
        }
        composable(
            "recipe_detail/{id}",
            arguments = listOf(navArgument("id"){ type = NavType.StringType })
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("id")
            val receta = listaTotalRecetas.find {it.id == recetaId }
            // Uso de condición if: verifica si la receta existe
            if (receta != null) {
                RecipeDetailScreen(
                    navController = navController,
                    receta = receta,
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
        // ruta para editar una receta existente
        composable(
            "nueva_minuta/{id}?editMode={editMode}",
            arguments = listOf(
                navArgument("id") {type = NavType.StringType; nullable = true},
                navArgument("editMode") {type = NavType.BoolType; defaultValue = false}
            )
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("id")
            val editMode = backStackEntry.arguments?.getBoolean("editMode") ?: false
            val recetaToEdit = if (recetaId != null) listaTotalRecetas.find { it.id == recetaId } else null

            NewMinutaScreen(
                navController = navController,
                onRecetaAgregada = { nuevaReceta ->
                    recetasUsuario.add(nuevaReceta)
                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                },
                onRecetaEditada = { recetaEditada ->
                    // Verificar si la receta es estatica (esta en recipes)
                    val isStaticRecipe = recipes.any {it.id == recetaEditada.id }
                    // Uso de condición if
                    if (isStaticRecipe){
                        // Si es estática, actualizar o agregar en recetasEstaticasEditadas
                        val index = recetasEstaticasEditadas.indexOfFirst { it.id == recetaEditada.id }
                        if (index != -1){
                            recetasEstaticasEditadas[index] = recetaEditada
                        }
                        else {
                            recetasEstaticasEditadas.add(recetaEditada)
                        }
                    }
                    else {
                        // Si la receta es creada por el usuario, actualizar en recetasUsuario
                        val index = recetasUsuario.indexOfFirst { it.id == recetaEditada.id }
                        if (index != -1){
                            recetasUsuario[index] = recetaEditada
                        }
                    }
                    navController.navigate("minuta") {popUpTo("minuta") {inclusive = false} }
                },
            recetaToEdit = recetaToEdit,
            editMode = editMode
            )
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
            // condición if: muestra el botón de retroceso si showBack es verdadero
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