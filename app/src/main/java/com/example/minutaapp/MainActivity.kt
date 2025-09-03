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

    // lista para compartir entre pantallas
    val recetasUsuario = remember { mutableStateListOf<Receta>() }

    // CONTROLAR LA NAVEGACION (rutas)
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN){
            LoginScreen (
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoForgot = { navController.navigate(Routes.FORGOT) },
                onLoginSuccess = { navController.navigate(Routes.MINUTA) }
            )
        }
        composable(Routes.REGISTER){
            RegisterScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.FORGOT){
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.MINUTA) {
            MinutaScreen(navController = navController, recetasUsuario = recetasUsuario)
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