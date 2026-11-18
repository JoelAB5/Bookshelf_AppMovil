package com.example.bookshelf

import android.app.Application
import com.example.bookshelf.data.BooksRepository
import com.example.bookshelf.data.NetworkBooksRepository
import com.example.bookshelf.network.BooksApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BookshelfApplication : Application() {
    val booksRepository: BooksRepository by lazy {
        val client = OkHttpClient.Builder().apply {
            val apiKey = BuildConfig.GOOGLE_BOOKS_API_KEY
            if (apiKey.isNotEmpty()) {
                addInterceptor { chain ->
                    val request = chain.request()
                    val url = request.url.newBuilder()
                        .addQueryParameter("key", apiKey)
                        .build()
                    chain.proceed(request.newBuilder().url(url).build())
                }
            }
        }.build()
        val service = Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BooksApiService::class.java)
        NetworkBooksRepository(service)
    }
}
