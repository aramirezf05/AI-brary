package com.example.ai_brary.data.remote

import com.example.ai_brary.data.model.GoogleBooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    // Fetches a book by ISBN
    @GET("volumes")
    suspend fun getBookByIsbn(
        @Query("q") isbn: String,    // The ISBN query parameter
        @Query("key") apiKey: String // Google Books API key
    ): Response<GoogleBooksResponse>
}
