package com.ltu.m7019e.moviedb.v24.database

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.ltu.m7019e.moviedb.v24.network.BookApiService
import com.ltu.m7019e.moviedb.v24.utils.Constants
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

interface AppContainer {
    val bookRepository : BookRepository
}

class DefaultAppContainer(private  val context : Context) : AppContainer {

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    val movieDBJson = Json {
        ignoreUnknownKeys = true
    }

    val gameDBJson = Json {
        ignoreUnknownKeys = true
    }

    val bookDBJson = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(movieDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.MOVIE_LIST_BASE_URL)
        .build()


    private val retrofit_books: Retrofit = Retrofit.Builder()
        .client(
            okhttp3.OkHttpClient.Builder()
                .addInterceptor(getLoggerInterceptor())
                .connectTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(20, java.util.concurrent.TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(bookDBJson.asConverterFactory("application/json".toMediaType()))
        .baseUrl(Constants.BOOK_LIST_BASE_URL)
        .build()




    private val bookretrofitService: BookApiService by lazy {
        retrofit_books.create(BookApiService::class.java)
    }

    override val bookRepository : BookRepository by lazy {
        NetworkGamesRepository(bookretrofitService)
    }



}