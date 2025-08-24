package com.example.minutaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.minutaapp.ui.theme.MinutaAppTheme

import androidx.navigation.compose.composable

import com.example.minutaapp.Routes

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinutaAppTheme {
                // llama a la funcion LoginApp
                //LoginApp()
                AppNav()

            }
        }
    }
}

// Navegacion y rutas
@Composable
fun AppNav(){
    var navController = rememberNavController()

    // CONTROLAR LA NAVEGACION
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ){
        composable(Routes.LOGIN){
            LoginApp(
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onGoForgot = { navController.navigate(Routes.FORGOT) }
            )
        }
        composable(Routes.REGISTER){
            RegisterScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.FORGOT){
            ForgotPasswordScreen(onBack = { navController.popBackStack() })
        }
    }
}


// Crear un TopBar reutilizable para las paginas RegisterScreen y ForgotPasswordScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    title: String,
    showBack: Boolean,
    onBack: (() -> Unit)? = null,
    onSettings: (() -> Unit)? = null,
    onMenu: (() -> Unit)? = null
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
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                }
            } else {
                IconButton(onClick = {onMenu?.invoke()}) {
                    Icon(Icons.Filled.Menu, contentDescription = "Menú")
                }
            }
        },
        actions = {
            IconButton(onClick = {onSettings?.invoke()}) {
                Icon(Icons.Filled.Settings, contentDescription = "Ajustes")
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



// Pantalla de Login
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginApp(
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit
){
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(
            // Titulo
            title = {
                Text("Minuta App",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold)
            },
            // Icono de navegacion
            navigationIcon = {
                IconButton(onClick = {/* TODO */}) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Menú"
                    )
                }
            },
            // Accion al presionar el icono
            actions = {
                IconButton(onClick = {/* TODO */}) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Ajustes"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        },
        /* elemento boton flotante */
        floatingActionButton = {
            /* etiqueta de material design: FloatingActionButton */
            FloatingActionButton(
                onClick = { },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Incrementar")
            }
        }
    ) {
        // Desde aqui parte el body de la aplicacion dentro de las llaves
            padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ){
            // Alineacion de los elementos en columnas
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.logo_nutri),
                    contentDescription = "Logo Nutrición",
                    modifier = Modifier.fillMaxWidth(0.5f), // ancho imagen
                    contentScale = ContentScale.Fit
                )

                // Campo Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("usuario")},
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña")},
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Botón
                Button(
                    onClick = { /* pag ppal */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }

                // Crear cuenta y olvidé contraseña + Navegacion
                TextButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
                    Text("Crear Cuenta")
                }
                TextButton(onClick = onGoForgot, modifier = Modifier.fillMaxWidth()) {
                    Text("¿Olvidaste tu contraseña?")
                }
            }
        }
    }
}


// Pantalla de Registro
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBack: () -> Unit){
    Scaffold(
        topBar = { SimpleTopBar(title = "Registro", showBack = true, onBack = onBack)}
    ) {
        padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ){
            Text(
                "Pantalla de Registro",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


// Pantalla de recuperar contraseña
@Composable
fun ForgotPasswordScreen(onBack: () -> Unit){
    var email by remember {mutableStateOf("")}

    Scaffold(
        topBar = { SimpleTopBar(title = "Recuperar Contraseña", showBack = true, onBack = onBack) }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { },
                modifier = Modifier.fillMaxWidth()){
                Text("Enviar instrucciones")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAppScaffold(){
    MaterialTheme {
        //LoginApp(onGoRegister = {}, onGoForgot = {})
        RegisterScreen{}
        //ForgotPasswordScreen {}
    }
}
