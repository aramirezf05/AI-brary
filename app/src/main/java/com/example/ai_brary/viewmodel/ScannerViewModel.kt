package com.example.ai_brary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai_brary.repository.BookRepository
import com.example.bibliotecaapp.data.model.Book
import kotlinx.coroutines.launch

class ScannerViewModel(private val repository: BookRepository) : ViewModel() {

    private val _scannedBook = MutableLiveData<Book?>()
    val scannedBook: LiveData<Book?> = _scannedBook

    private val _errorMessage = MutableLiveData<String?>()

    fun fetchBookByIsbn(isbn: String) {
        viewModelScope.launch {
            try {
                val book = repository.getBookByIsbn(isbn)
                if (book != null) {
                    _scannedBook.postValue(book)
                } else {
                    _errorMessage.postValue("Libro no encontrado con ISBN: $isbn")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error al buscar el libro: ${e.message}")
            }
        }
    }
}
