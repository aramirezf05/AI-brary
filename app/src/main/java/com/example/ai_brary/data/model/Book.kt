package com.example.bibliotecaapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class Book(
    @PrimaryKey val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val thumbnailUrl: String?,
    val publishedDate: String?,
    val isbn: String
)
