package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApiService

data class Book(val id: String, val title: String, val thumbnailUrl: String?)

interface BooksRepository {
    suspend fun getBooks(query: String): List<Book>
}

class NetworkBooksRepository(private val service: BooksApiService) : BooksRepository {
    override suspend fun getBooks(query: String): List<Book> {
        val references = service.searchBooks(query).items.orEmpty()
        val books = mutableListOf<Book>()
        for (reference in references.distinctBy { it.id }) {
            val volume = service.getBook(reference.id).volumeInfo
            val thumbnailUrl = volume.imageLinks?.thumbnail?.let { thumbnail ->
                if (thumbnail.startsWith("http://")) {
                    thumbnail.replaceFirst("http://", "https://")
                } else {
                    thumbnail
                }
            }
            books.add(Book(reference.id, volume.title, thumbnailUrl))
        }
        return books
    }
}
