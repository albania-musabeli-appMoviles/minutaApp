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
import com.example.minutaapp.ui.theme.MinutaAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinutaAppTheme {

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

                        }
                    }
                }
            }
        }
    }
}

