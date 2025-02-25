package com.example.ai_brary.repository

import com.example.ai_brary.data.local.BookDao
import com.example.ai_brary.data.model.toBook
import com.example.ai_brary.data.remote.ApiService
import com.example.ai_brary.data.model.Book

class BookRepository(
    private val bookDao: BookDao,
    private val apiService: ApiService
) {

    /**
     * Searches for a book by its ISBN, first in the local database
     * and if not found, it performs a remote query to the Google Books API.
     */
    suspend fun getBookByIsbn(isbn: String): Book? {
        // First, try to get the book from the local database
        val localBook = bookDao.getBookByIsbn(isbn)
        if (localBook != null) {
            return localBook // If found locally, return it
        }

        // If not found locally, search it via the API (Google Books)
        val remoteBook = fetchBookFromApi(isbn)
        remoteBook?.let {
            // If the book is found via the API, save it in the local database
            bookDao.insertBook(it)
        }

        return remoteBook // Return the book found in the API (or null if not found)
    }

    private suspend fun fetchBookFromApi(isbn: String): Book? {
        return try {
            val response = apiService.getBookByIsbn(isbn, "API_KEY")

            if (response.isSuccessful && response.body() != null) {
                val volumeInfo = response.body()?.items?.firstOrNull()?.volumeInfo
                volumeInfo?.toBook(isbn)
            } else {
                null // Return null if the response is empty or unsuccessful
            }
        } catch (e: Exception) {
            // Handle any exceptions (e.g., network error)
            null
        }
    }

}
