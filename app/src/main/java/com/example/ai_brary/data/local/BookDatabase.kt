package com.example.ai_brary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ai_brary.data.model.Book

@Database(entities = [Book::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        // Función para obtener una instancia de la base de datos
        fun getInstance(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                // Si no existe una instancia, se crea
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database" // Nombre del archivo de base de datos
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}