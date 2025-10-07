package com.example.minutaapp

import android.content.Context
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
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
import com.example.minutaapp.data.RecetaDbHelper

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
    var selectedColor by rememberSaveable { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val recetas = remember { mutableStateListOf<Receta>() }
    var correoUsuario by remember { mutableStateOf<String?>(null) }


    // Obtener el correo del usuario autenticado desde SharedPreferences
    // LaunchedEffect es para cargar las recetas del usuario autenticado al iniciar la aplicación
    LaunchedEffect(Unit) {
        val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        correoUsuario = prefs.getString("correo", null)
        correoUsuario?.let { correo ->
            RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                recetas.clear()
                recetas.addAll(recetasDb)
            }
        }
    }


    // CONTROLAR LA NAVEGACION (rutas)
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN) {
            LoginScreen(
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoForgot = { navController.navigate(Routes.FORGOT) },
                onLoginSuccess = {
                    // Actualizar correoUsuario y cargar recetas
                    val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    correoUsuario = prefs.getString("correo", null)
                    correoUsuario?.let { correo ->
                        RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                            recetas.clear()
                            recetas.addAll(recetasDb)
                            navController.navigate(Routes.MINUTA)
                        }
                    }
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.FORGOT) {
            ForgotPasswordScreen(onBack = { navController.popBackStack() }, navController = navController)
        }
        composable(Routes.MINUTA) {
            correoUsuario?.let { correo ->
                MinutaScreen(
                    navController = navController,
                    recetas = recetas,
                    usuarioCorreo = correo, // Pasar usuarioCorreo
                    selectedColor = selectedColor,
                    onColorSelected = { newColor -> selectedColor = newColor }
                )
            } ?: run {
                Text("Error: Debe iniciar sesión para ver minutas")
            }
        }
        composable(Routes.NEWMINUTA) {
            correoUsuario?.let { correo ->
                NewMinutaScreen(
                    navController = navController,
                    onRecetaAgregada = { nuevaReceta ->
                        // asociar la receta con el correo del usuario
                        RecetaDbHelper.guardarReceta(context, nuevaReceta, false) { result ->
                            if (result.ok) {
                                RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                                    recetas.clear()
                                    recetas.addAll(recetasDb)
                                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                                }
                            } else {
                                Toast.makeText(context, result.mensaje ?: "Error al guardar", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onRecetaEditada = { recetaEditada ->
                        // asociar la receta con el correo del usuario
                        RecetaDbHelper.guardarReceta(context, recetaEditada, true) { result ->
                            if (result.ok) {
                                RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                                    recetas.clear()
                                    recetas.addAll(recetasDb)
                                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                                }
                            } else {
                                Toast.makeText(context, result.mensaje ?: "Error al actualizar", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    recetaToEdit = null,
                    editMode = false,
                    usuarioCorreo = correo
                )
            }
        }
        composable(
            "recipe_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("id")
            val receta = recetas.find { it.id == recetaId }
            if (receta != null) {
                RecipeDetailScreen(
                    navController = navController,
                    receta = receta,
                    onEliminarReceta = { receta ->
                        correoUsuario?.let { correo ->
                            RecetaDbHelper.eliminarReceta(context, receta) { result ->
                                if (result.ok) {
                                    RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                                        recetas.clear()
                                        recetas.addAll(recetasDb)
                                        navController.popBackStack()
                                    }
                                } else {
                                    Toast.makeText(context, result.mensaje ?: "Error al eliminar", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                )
            } else {
                Text("Error: Receta no encontrada")
            }
        }
        // ruta para editar una receta existente
        composable(
            "nueva_minuta/{id}?editMode={editMode}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType; nullable = true },
                navArgument("editMode") { type = NavType.BoolType; defaultValue = false }
            )
        ) { backStackEntry ->
            val recetaId = backStackEntry.arguments?.getString("id")
            val editMode = backStackEntry.arguments?.getBoolean("editMode") ?: false
            val recetaToEdit = recetas.find { it.id == recetaId }
            correoUsuario?.let { correo ->
                NewMinutaScreen(
                    navController = navController,
                    onRecetaAgregada = { nuevaReceta ->
                        RecetaDbHelper.guardarReceta(context, nuevaReceta.copy(usuarioCorreo = correo), false) { result ->
                            if (result.ok) {
                                RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                                    recetas.clear()
                                    recetas.addAll(recetasDb)
                                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                                }
                            } else {
                                Toast.makeText(context, result.mensaje ?: "Error al guardar", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    onRecetaEditada = { recetaEditada ->
                        RecetaDbHelper.guardarReceta(context, recetaEditada.copy(usuarioCorreo = correo), true) { result ->
                            if (result.ok) {
                                RecetaDbHelper.obtenerRecetas(context, correo) { recetasDb ->
                                    recetas.clear()
                                    recetas.addAll(recetasDb)
                                    navController.navigate("minuta") { popUpTo("minuta") { inclusive = false } }
                                }
                            } else {
                                Toast.makeText(context, result.mensaje ?: "Error al actualizar", Toast.LENGTH_LONG).show()
                            }
                        }
                    },
                    recetaToEdit = recetaToEdit,
                    editMode = editMode,
                    usuarioCorreo = correo
                )
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