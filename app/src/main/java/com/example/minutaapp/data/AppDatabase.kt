package com.example.minutaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// BASE DE DATOS LOCAL CON ROOM (persitencia de datos a largo plazo)

// Aqui se agrega el objeto con el que se conectará a la base de datos
// Esta es una instancia de base de datos
@Database(entities = [Usuario::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // retorna una instancia que se va a conectar  a mi bbdd local app_db ligera
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}