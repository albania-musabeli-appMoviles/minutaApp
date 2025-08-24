package com.example.minutaapp.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minutaapp.SimpleTopBar

val registros = mutableListOf<Map<String, String>>()

fun guardarRegistro(
    nombre: String,
    correo: String,
    password: String,
    pais: String,
    noticias: Boolean,
    ofertas: Boolean
) {
    val registro = mapOf(
        "nombre" to nombre,
        "correo" to correo,
        "password" to password,
        "pais" to pais,
        "recibeNoticias" to noticias.toString(),
        "recibeOfertas" to ofertas.toString()
    )
    registros.add(registro)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val paises = listOf("Chile", "Argentina", "Perú", "México")
    var paisExpanded by remember { mutableStateOf(false) }
    var paisSelected by remember { mutableStateOf(paises.first()) }
    var recibeNoticias by remember { mutableStateOf(false) }
    var recibeOfertas by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Scaffold(
        topBar = { SimpleTopBar(title = "Registro", showBack = true, onBack = onBack) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Formulario de Registro", fontSize = 22.sp)
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
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
                ExposedDropdownMenuBox(
                    expanded = paisExpanded,
                    onExpandedChange = { paisExpanded = !paisExpanded }
                ) {
                    OutlinedTextField(
                        value = paisSelected,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("País") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = paisExpanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = paisExpanded,
                        onDismissRequest = { paisExpanded = false }
                    ) {
                        paises.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    paisSelected = opcion
                                    paisExpanded = false
                                }
                            )
                        }
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = recibeNoticias, onCheckedChange = { recibeNoticias = it })
                    Text("Recibir noticias")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = recibeOfertas, onCheckedChange = { recibeOfertas = it })
                    Text("Recibir ofertas")
                }
                Text(
                    text = "Leer términos y condiciones",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        uriHandler.openUri("https://www.example.com/terminos")
                    }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            guardarRegistro(nombre, correo, password, paisSelected, recibeNoticias, recibeOfertas)
                            Toast.makeText(
                                context,
                                "Datos guardados correctamente. Total registros: ${registros.size}",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Registrarme")
                    }
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar")
                    }
                }
            }
        }
    }
}