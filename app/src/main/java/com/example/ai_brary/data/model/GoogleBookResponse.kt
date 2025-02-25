package com.example.ai_brary.data.model

data class GoogleBooksResponse(
    val items: List<BookItem>?
)

// Model for each item in the response
data class BookItem(
    val id: String,               // The unique ID from Google Books
    val volumeInfo: VolumeInfo    // The detailed book info
)

data class VolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?,
    val publishedDate: String?,
    val id: String?
)

data class ImageLinks(
    val thumbnail: String?
)

fun VolumeInfo.toBook(isbn: String): Book {
    return Book(
        id = id ?: "Unknown ID", // Default value for ID
        isbn = isbn, // Use the ISBN passed from the API response
        title = title ?: "Unknown Title", // Default value for title if null
        author = authors?.joinToString(", ") ?: "Unknown Author", // Default value for authors
        description = description ?: "No description available", // Default description
        thumbnailUrl = imageLinks?.thumbnail, // Thumbnail from imageLinks
        publishedDate = publishedDate ?: "Unknown Date" // Default value for publishedDate
    )
}
