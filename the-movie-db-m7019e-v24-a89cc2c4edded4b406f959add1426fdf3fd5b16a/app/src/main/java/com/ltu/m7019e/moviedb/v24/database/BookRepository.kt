package com.ltu.m7019e.moviedb.v24.database

import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse
import com.ltu.m7019e.moviedb.v24.network.BookApiService

interface BookRepository {
    suspend fun getTrendingWorks() : TrendingWorksQueryResponse

}

class NetworkGamesRepository(private val apiService: BookApiService) : BookRepository {




    override suspend fun getTrendingWorks() : TrendingWorksQueryResponse{
        return apiService.getTrendingWorks()
    }





}
