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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.minutaapp.ui.theme.MinutaAppTheme

import androidx.navigation.compose.composable

import com.example.minutaapp.screens.ForgotPasswordScreen
import com.example.minutaapp.screens.MinutaScreen
import com.example.minutaapp.screens.RegisterScreen

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

    // CONTROLAR LA NAVEGACION
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
            MinutaScreen(navController = navController)
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


//
//// Pantalla de Login
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LoginApp(
//    onGoRegister: () -> Unit,
//    onGoForgot: () -> Unit
//){
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    Scaffold(
//        topBar = { TopAppBar(
//            // Titulo
//            title = {
//                Text("Minuta App",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.SemiBold)
//            },
//            // Icono de navegacion
//            navigationIcon = {
//                IconButton(onClick = {/* TODO */}) {
//                    Icon(
//                        imageVector = Icons.Filled.Menu,
//                        contentDescription = "Menú"
//                    )
//                }
//            },
//            // Accion al presionar el icono
//            actions = {
//                IconButton(onClick = {/* TODO */}) {
//                    Icon(
//                        imageVector = Icons.Filled.Settings,
//                        contentDescription = "Ajustes"
//                    )
//                }
//            },
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                titleContentColor = MaterialTheme.colorScheme.onPrimary,
//                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
//                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
//            )
//        )
//        },
//        /* elemento boton flotante */
//        floatingActionButton = {
//            /* etiqueta de material design: FloatingActionButton */
//            FloatingActionButton(
//                onClick = { },
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                elevation = FloatingActionButtonDefaults.elevation(
//                    defaultElevation = 6.dp,
//                    pressedElevation = 8.dp
//                ),
//                shape = MaterialTheme.shapes.large
//            ) {
//                Icon(Icons.Filled.Add, contentDescription = "Incrementar")
//            }
//        }
//    ) {
//        // Desde aqui parte el body de la aplicacion dentro de las llaves
//            padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            contentAlignment = Alignment.Center
//        ){
//            // Alineacion de los elementos en columnas
//            Column (
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 32.dp),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Logo
//                Image(
//                    painter = painterResource(id = R.drawable.logo_nutri),
//                    contentDescription = "Logo Nutrición",
//                    modifier = Modifier.fillMaxWidth(0.5f), // ancho imagen
//                    contentScale = ContentScale.Fit
//                )
//
//                // Campo Usuario
//                OutlinedTextField(
//                    value = username,
//                    onValueChange = { username = it },
//                    label = { Text("usuario")},
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Text,
//                        imeAction = ImeAction.Next
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Campo Contraseña
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Contraseña")},
//                    singleLine = true,
//                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Botón
//                Button(
//                    onClick = { /* pag ppal */ },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("Iniciar Sesión")
//                }
//
//                // Crear cuenta y olvidé contraseña + Navegacion
//                TextButton(onClick = onGoRegister, modifier = Modifier.fillMaxWidth()) {
//                    Text("Crear Cuenta")
//                }
//                TextButton(onClick = onGoForgot, modifier = Modifier.fillMaxWidth()) {
//                    Text("¿Olvidaste tu contraseña?")
//                }
//            }
//        }
//    }
//}
//
//
////// Pantalla de Registro
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RegisterScreen(onBack: () -> Unit){
//
//    // variables para registro
//    var nombre by remember { mutableStateOf("") }
//    var correo by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    // Combo
//    val paises = listOf("Chile", "Argentina", "Perú", "México")
//    var paisExpanded by remember { mutableStateOf(false) }
//    var paisSelected by remember { mutableStateOf(paises.first()) }
//
//    var recibeNoticias by remember { mutableStateOf(false) }
//    var recibeOfertas by remember { mutableStateOf(false) }
//
//    // para crear enlaces dentro de la app
//    val uriHandler = LocalUriHandler.current
//
//    val context = LocalContext.current
//
//    Scaffold(
//        topBar = { SimpleTopBar(title = "Registro", showBack = true, onBack = onBack)}
//    ) {
//        padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding),
//            contentAlignment = Alignment.Center
//        ){
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ){
//                // Encabezado
//                Text("Formulario de Registro", fontSize = 22.sp)
//
//                // nombre
//                OutlinedTextField(
//                    value = nombre,
//                    onValueChange = { nombre = it },
//                    label = { Text("Nombre Completo") },
//                    singleLine = true,
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // correo
//                OutlinedTextField(
//                    value = correo,
//                    onValueChange = { correo = it },
//                    label = { Text("Correo")},
//                    singleLine = true,
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Email,
//                        imeAction = ImeAction.Next
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Contraseña
//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text("Contraseña")},
//                    singleLine = true,
//                    visualTransformation = PasswordVisualTransformation(),
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                ExposedDropdownMenuBox(
//                    expanded = paisExpanded,
//                    onExpandedChange = { paisExpanded = !paisExpanded }
//                ) {
//                    OutlinedTextField(
//                        value = paisSelected,
//                        onValueChange = {},
//                        readOnly = true,
//                        label = { Text("País")},
//                        trailingIcon = {
//                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = paisExpanded)
//                        },
//                        modifier = Modifier.menuAnchor().fillMaxWidth()
//                    )
//                    ExposedDropdownMenu(
//                        expanded = paisExpanded,
//                        onDismissRequest = { paisExpanded = false }
//                    ){
//                        paises.forEach { opcion ->
//                            DropdownMenuItem(
//                                text = { Text(opcion) },
//                                onClick = {
//                                    paisSelected = opcion
//                                    paisExpanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//
//                // Check - Los elementos Row ordenan los elementos de izquierda a derecha
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    // un elemento es el checkbox
//                    Checkbox(checked = recibeNoticias, onCheckedChange = { recibeNoticias = it })
//                    // otro elemento es el texto
//                    Text("Recibir noticias")
//                }
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Checkbox(checked = recibeOfertas, onCheckedChange = { recibeOfertas = it })
//                    Text("Recibir ofertas")
//                }
//
//                // Link
//                Text(
//                    text = "Leer términos y condiciones",
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.clickable {
//                        uriHandler.openUri("https://www.example.com/terminos")
//                    }
//                )
//
//                // Botones
//                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)){
//                    Button(
//                        onClick = {
//                            /* Registrar */
//                            guardarRegistro(nombre, correo, password, paisSelected, recibeNoticias, recibeOfertas)
//                            Toast.makeText(
//                                context,
//                                "Datos guardados correctamente. Total registros: ${registros.size}",
//                                Toast.LENGTH_LONG
//                            ).show()
//                        },
//                        modifier = Modifier.weight(1f)
//                    ) {Text("Registrarme")}
//
//                    OutlinedButton(
//                        onClick = onBack,
//                        modifier = Modifier.weight(1f)
//                    ){ Text("Cancelar") }
//                }
//
//
//            }
//
//            /*Text(
//                "Pantalla de Registro",
//                style = MaterialTheme.typography.bodyLarge
//            )*/
//        }
//    }
//}
//
//
//// Declarar variable global para el arreglo (siempre antes de cualquier Composable)
//val registros = mutableListOf<Map<String, String>>()
//
//
//fun guardarRegistro(nombre: String, correo: String, password: String, pais: String, noticias: Boolean, ofertas: Boolean){
//    val registro = mapOf(
//        "nombre" to nombre,
//        "correo" to correo,
//        "password" to password,
//        "pais" to pais,
//        "recibeNoticias" to noticias.toString(),
//        "recibeOfertas" to ofertas.toString()
//    )
//    // agregar a la lista
//    registros.add(registro)
//}
//
//
//
//
//// Pantalla de recuperar contraseña
//@Composable
//fun ForgotPasswordScreen(onBack: () -> Unit){
//    var email by remember {mutableStateOf("")}
//
//    Scaffold(
//        topBar = { SimpleTopBar(title = "Recuperar Contraseña", showBack = true, onBack = onBack) }
//    ){ padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .padding(horizontal = 24.dp, vertical = 16.dp),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ){
//            OutlinedTextField(
//                value = email,
//                onValueChange = { email = it },
//                label = { Text("Correo Electrónico") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Email,
//                    imeAction = ImeAction.Done
//                ),
//                modifier = Modifier.fillMaxWidth()
//            )
//
//            Button(onClick = { },
//                modifier = Modifier.fillMaxWidth()){
//                Text("Enviar instrucciones")
//            }
//        }
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewAppScaffold(){
//    MaterialTheme {
//        //LoginApp(onGoRegister = {}, onGoForgot = {})
//        RegisterScreen{}
//        //ForgotPasswordScreen {}
//    }
//}
