package com.example.bookshelf.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BooksApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): BooksSearchResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): VolumeResponse
}

data class BooksSearchResponse(val items: List<VolumeReference>?)

data class VolumeReference(val id: String)

data class VolumeResponse(val volumeInfo: VolumeInfo)

data class VolumeInfo(val title: String, val imageLinks: ImageLinks?)

data class ImageLinks(val thumbnail: String?)
