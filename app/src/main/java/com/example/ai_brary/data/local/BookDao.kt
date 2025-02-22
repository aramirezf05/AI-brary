package com.example.ai_brary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.bibliotecaapp.data.model.Book

@Dao
interface BookDao {

    // Insert a book into the database
    @Insert
    suspend fun insertBook(book: Book)

    // Insert multiple books into the database (optional, for bulk insert)
    @Insert
    suspend fun insertBooks(books: List<Book>)

    // Get a book by its ISBN
    @Query("SELECT * FROM books WHERE isbn = :isbn LIMIT 1")
    suspend fun getBookByIsbn(isbn: String): Book?

    // Get all books from the database
    @Query("SELECT * FROM books")
    suspend fun getAllBooks(): List<Book>

    // Update a book (if it already exists)
    @Update
    suspend fun updateBook(book: Book)

    // Delete a book by its ISBN
    @Query("DELETE FROM books WHERE isbn = :isbn")
    suspend fun deleteBookByIsbn(isbn: String)
}
