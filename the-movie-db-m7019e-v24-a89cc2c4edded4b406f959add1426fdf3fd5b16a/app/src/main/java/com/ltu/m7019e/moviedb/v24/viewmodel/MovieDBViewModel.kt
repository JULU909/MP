package com.ltu.m7019e.moviedb.v24.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.ltu.m7019e.moviedb.v24.MovieDBApplication
import com.ltu.m7019e.moviedb.v24.database.BookRepository


import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse

import com.ltu.m7019e.moviedb.v24.model.Works
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BooksUIState {

    data class Success(
        val bookResponse: TrendingWorksQueryResponse,
        val works: List<Works>
    ) : BooksUIState
    object Error : BooksUIState
    object Loading : BooksUIState
}


class MovieDBViewModel( private val bookRepository: BookRepository) : ViewModel() {



    var bookUiState: BooksUIState by mutableStateOf(BooksUIState.Loading)
        private set



    init {
        getBooks();
    }



    fun getBooks(){

        viewModelScope.launch {
            bookUiState =BooksUIState.Loading
            bookUiState = try {
                BooksUIState.Success(bookRepository.getTrendingWorks(), bookRepository.getTrendingWorks().works)
            } catch (e: IOException) {
                BooksUIState.Error
            } catch (e: HttpException) {
                BooksUIState.Error
            }
        }
    }




    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val bookRepository = application.container.bookRepository

                MovieDBViewModel( bookRepository = bookRepository)
            }
        }
    }
}