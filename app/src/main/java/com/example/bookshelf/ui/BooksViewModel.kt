package com.example.bookshelf.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.Book
import com.example.bookshelf.data.BooksRepository
import com.google.gson.JsonParseException
import java.io.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

sealed interface BooksUiState {
    data object Loading : BooksUiState
    data class Success(val books: List<Book>) : BooksUiState
    data object Error : BooksUiState
}

class BooksViewModel(private val repository: BooksRepository) : ViewModel() {
    var uiState: BooksUiState by mutableStateOf(BooksUiState.Loading)
        private set

    init {
        loadBooks()
    }

    fun loadBooks() {
        viewModelScope.launch {
            uiState = BooksUiState.Loading
            uiState = try {
                BooksUiState.Success(repository.getBooks("jazz history"))
            } catch (_: IOException) {
                BooksUiState.Error
            } catch (_: HttpException) {
                BooksUiState.Error
            } catch (_: JsonParseException) {
                BooksUiState.Error
            }
        }
    }
}
