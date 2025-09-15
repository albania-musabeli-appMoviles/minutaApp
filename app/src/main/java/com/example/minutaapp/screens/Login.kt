package com.example.minutaapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.minutaapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onGoRegister: () -> Unit,
    onGoForgot: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    // variables para almacenar los valores de los campos del formulario
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current // hace referencia a la vista para mostrar el Toast
    
    // Constantes que simulan credenciales de una bbdd (val no cambia)
    val userBD = "admin"
    val passwordBD = "1234"


    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Logo de la aplicación
                Image(
                    painter = painterResource(id = R.drawable.logo_nutri),
                    contentDescription = "Logo Nutrición",
                    modifier = Modifier.fillMaxWidth(0.5f)
                )
                // Campo para usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                // Campo para contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        // Condición if: Verifica si los campos están vacios
                        if (username.isBlank() || password.isBlank()){
                            Toast.makeText(
                                context,
                                "Los campos no pueden estar vacíos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        // Condición if: verifica si las credenciales no coinciden con la bbdd
                        else if (username != userBD || password != passwordBD){
                            Toast.makeText(
                                context,
                                "Usuario o contraseña incorrectos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else {
                            onLoginSuccess() // redirigir a pantalla principal
                        }
                              },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
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