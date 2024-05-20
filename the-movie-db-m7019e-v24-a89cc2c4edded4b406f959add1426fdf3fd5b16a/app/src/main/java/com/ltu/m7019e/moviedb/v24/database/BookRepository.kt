package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.Author
import com.ltu.m7019e.moviedb.v24.model.AuthorWorksQueryResponse
import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse
import com.ltu.m7019e.moviedb.v24.network.BookApiService

interface BookRepository {
    suspend fun getTrendingWorks() : TrendingWorksQueryResponse

    suspend fun getClassicWorks() : TrendingWorksQueryResponse

    suspend fun getAuthor(id:String): Author

    suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse

}

class NetworkGamesRepository(private val apiService: BookApiService) : BookRepository {




    override suspend fun getTrendingWorks() : TrendingWorksQueryResponse{
        return apiService.getTrendingWorks()
    }

    override suspend fun getClassicWorks() : TrendingWorksQueryResponse{
        return apiService.getClassicWorks()
    }

    override suspend fun getAuthor(id:String): Author {
        return apiService.getAuthorInformation(id)
    }

    override suspend fun getAuthorWorks(id: String): AuthorWorksQueryResponse {
        return apiService.getAuthorWorks(id)
    }



}
