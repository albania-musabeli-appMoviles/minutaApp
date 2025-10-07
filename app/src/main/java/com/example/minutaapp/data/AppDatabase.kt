package com.example.minutaapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// BASE DE DATOS LOCAL CON ROOM (persitencia de datos a largo plazo)

// Aqui se agrega el objeto con el que se conectará a la base de datos
// Esta es una instancia de base de datos
@Database(entities = [Usuario::class, Receta::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun recetaDao(): RecetaDao

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
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE recetas (
                id TEXT PRIMARY KEY NOT NULL,
                nombre TEXT NOT NULL,
                ingredientes TEXT NOT NULL,
                tipo TEXT NOT NULL,
                recomendacionNutricional TEXT NOT NULL,
                usuarioCorreo TEXT NOT NULL
            )
        """)
    }
}