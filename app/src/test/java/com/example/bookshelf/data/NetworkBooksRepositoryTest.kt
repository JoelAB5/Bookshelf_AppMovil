package com.example.bookshelf.data

import com.example.bookshelf.network.BooksApiService
import com.example.bookshelf.network.BooksSearchResponse
import com.example.bookshelf.network.ImageLinks
import com.example.bookshelf.network.VolumeInfo
import com.example.bookshelf.network.VolumeReference
import com.example.bookshelf.network.VolumeResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBooksRepositoryTest {
    @Test
    fun searchesThenFetchesEachVolumeInOrderAndUsesHttpsThumbnails() = runTest {
        val service = FakeBooksApiService(
            mapOf(
                "first" to VolumeInfo("Jazz History", ImageLinks("http://example.com/first")),
                "second" to VolumeInfo("Jazz Piano", ImageLinks("https://example.com/second")),
                "third" to VolumeInfo("Jazz Styles", null),
            ),
        )

        val books = NetworkBooksRepository(service).getBooks("jazz history")

        assertEquals(listOf("search:jazz history", "first", "second", "third"), service.calls)
        assertEquals(
            listOf(
                Book("first", "Jazz History", "https://example.com/first"),
                Book("second", "Jazz Piano", "https://example.com/second"),
                Book("third", "Jazz Styles", null),
            ),
            books,
        )
    }

    @Test
    fun absentSearchItemsReturnAnEmptyListWithoutFetchingDetails() = runTest {
        val service = FakeBooksApiService(emptyMap())

        assertEquals(emptyList<Book>(), NetworkBooksRepository(service).getBooks("unknown"))
        assertEquals(listOf("search:unknown"), service.calls)
    }

    private class FakeBooksApiService(
        private val volumes: Map<String, VolumeInfo>,
    ) : BooksApiService {
        val calls = mutableListOf<String>()

        override suspend fun searchBooks(query: String): BooksSearchResponse {
            calls.add("search:$query")
            val items = volumes.keys.map(::VolumeReference).ifEmpty { null }
            return BooksSearchResponse(items)
        }

        override suspend fun getBook(id: String): VolumeResponse {
            calls.add(id)
            return VolumeResponse(volumes.getValue(id))
        }
    }
}
