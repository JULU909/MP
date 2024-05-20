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
import com.ltu.m7019e.moviedb.v24.model.Author


import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse
import com.ltu.m7019e.moviedb.v24.model.Work

import com.ltu.m7019e.moviedb.v24.model.Works
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BooksUIState {

    data class Success(
        val bookResponse: TrendingWorksQueryResponse,
        val works: List<Works>,
        val classicBookResponse: TrendingWorksQueryResponse,
        val classicWorks : List<Works>,

    ) : BooksUIState
    object Error : BooksUIState
    object Loading : BooksUIState
}

sealed interface SelectedBookUIState {

    data class Success(
        val work : Works,
    ) : SelectedBookUIState
    object Error : SelectedBookUIState
    object Loading : SelectedBookUIState
}


sealed interface FavouriteBookStateUI {

    data class Success(
        val favWork : List<Works>,
    ) : FavouriteBookStateUI
    object Error : FavouriteBookStateUI
    object Loading : FavouriteBookStateUI
}

sealed interface SelectedAuthorUiState {
    data class Success(
        val author: Author,
        val works: List<Work>
    ) : SelectedAuthorUiState
    object Error : SelectedAuthorUiState
    object Loading : SelectedAuthorUiState
}



class MovieDBViewModel( private val bookRepository: BookRepository) : ViewModel() {



    var bookUiState: BooksUIState by mutableStateOf(BooksUIState.Loading)
        private set

    var selectedBookUIState: SelectedBookUIState by mutableStateOf(SelectedBookUIState.Loading)
        private set

    var favouriteBookStateUI: FavouriteBookStateUI by mutableStateOf(FavouriteBookStateUI.Loading)
        private set

    var selectedAuthorUiState: SelectedAuthorUiState by mutableStateOf(SelectedAuthorUiState.Loading)
        private set


    private val favouriteWorks = mutableListOf<Works>()


    init {
        getBooks();
        initializeFavourites();
    }



    fun getBooks(){
        viewModelScope.launch {
            bookUiState =BooksUIState.Loading
            bookUiState = try {
                BooksUIState.Success(bookRepository.getTrendingWorks(), bookRepository.getTrendingWorks().works,bookRepository.getClassicWorks() , bookRepository.getClassicWorks().works)
            } catch (e: IOException) {
                BooksUIState.Error
            } catch (e: HttpException) {
                BooksUIState.Error
            }
        }
    }

    fun initializeFavourites(){
        viewModelScope.launch {
            favouriteBookStateUI =FavouriteBookStateUI.Loading
            favouriteBookStateUI = try {
                FavouriteBookStateUI.Success(favouriteWorks)
            } catch (e: IOException) {
                FavouriteBookStateUI.Error
            } catch (e: HttpException) {
                FavouriteBookStateUI.Error
            }
        }
    }


    fun selectWork(work: Works){
        viewModelScope.launch {
            selectedBookUIState =SelectedBookUIState.Loading
            selectedBookUIState = try {
                SelectedBookUIState.Success(work)
            } catch (e: IOException) {
                SelectedBookUIState.Error
            } catch (e: HttpException) {
                SelectedBookUIState.Error
            }
        }
    }



    fun addFavourite(work: Works) {
        if (!favouriteWorks.contains(work)) {
            favouriteWorks.add(work)
        }

        viewModelScope.launch {
            favouriteBookStateUI =FavouriteBookStateUI.Loading
            favouriteBookStateUI = try {
                FavouriteBookStateUI.Success(favouriteWorks)
            } catch (e: IOException) {
                FavouriteBookStateUI.Error
            } catch (e: HttpException) {
                FavouriteBookStateUI.Error
            }
        }


    }

    fun getFavouriteWorks(): List<Works> {
        return favouriteWorks.toList() // Return a copy of the list to avoid modification from outside
    }

    fun removeFavourite(work: Works) {
        favouriteWorks.remove(work)
    }

    fun setSelectedAuthor(key: String) {
        viewModelScope.launch {
            println("Author : " )

            selectedAuthorUiState = SelectedAuthorUiState.Loading
            selectedAuthorUiState = try {
                val author = bookRepository.getAuthor(key)
                val works = bookRepository.getAuthorWorks(key)
                SelectedAuthorUiState.Success(author, works.entries)
            } catch (e: IOException) {
                SelectedAuthorUiState.Error
            } catch (e: HttpException) {
                SelectedAuthorUiState.Error
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