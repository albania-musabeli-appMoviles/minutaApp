package com.example.minutaapp

import com.example.minutaapp.auth.AuthRepository
import com.example.minutaapp.auth.LoginResult
import com.example.minutaapp.auth.LoginViewModel
import com.example.minutaapp.auth.Usuario
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class LoginViewModelTest {

    private val repo = mock<AuthRepository>()
    private val viewModel = LoginViewModel(repo)

    @Test
    fun `correo no existe`() {
        // lo que le envio a la prueba
        whenever(repo.autenticar("albania@gmail.com", "123456"))
            .thenReturn(LoginResult(false, "Correo no existe"))

        // resultado esperado
        val resultado = viewModel.login("albania@gmail.com", "123456")

        assertFalse(resultado.ok)
        assertEquals("Correo no existe", resultado.mensaje)
    }

    @Test
    fun `password incorrecta`() {
        whenever(repo.autenticar("albania@gmail.com", "error"))
            .thenReturn(LoginResult(false, "Contraseña incorrecta"))

        val resultado = viewModel.login("albania@gmail.com", "error")

        assertFalse(resultado.ok)
        assertEquals("Contraseña incorrecta", resultado.mensaje)
    }
    

    @Test
    fun `login correcto`() {
        val user = Usuario("albania@gmail.com", "123456")
        whenever(repo.autenticar("albania@gmail.com", "123456"))
            .thenReturn(LoginResult(true, usuario = user))

        val resultado = viewModel.login("albania@gmail.com", "123456")

        Assert.assertTrue(resultado.ok)
        assertEquals("albania@gmail.com", resultado.usuario?.correo)
    }


    @Test
    fun `campos vacios`(){
        val resultado = viewModel.login("", "")

        assertFalse(resultado.ok)
        // mensaje que se le envia para probar
        assertEquals("Campos vacíos", resultado.mensaje)
        verify(repo, never()).autenticar(any(), any())
    }
}