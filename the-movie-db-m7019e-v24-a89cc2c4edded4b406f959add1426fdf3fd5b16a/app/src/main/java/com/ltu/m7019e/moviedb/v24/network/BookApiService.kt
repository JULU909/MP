package com.ltu.m7019e.moviedb.v24.network

import com.ltu.m7019e.moviedb.v24.model.Author
import com.ltu.m7019e.moviedb.v24.model.AuthorWorksQueryResponse
import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query




interface BookApiService {


    @GET("trending/{Time}.json")
    suspend fun getTrendingWorks(
        @Path("Time")
        time: String = "weekly",
        @Query("limit")
        limit: String = "10",
    ): TrendingWorksQueryResponse

    @GET("trending/{Time}.json")
    suspend fun getClassicWorks(
        @Path("Time")
        time: String = "decade",
        @Query("limit")
        limit: String = "10",
    ): TrendingWorksQueryResponse

    @GET("authors/{Id}.json")
    suspend fun getAuthorInformation(
        @Path("Id")
        id: String,
    ): Author

    @GET("authors/{Id}/works.json")
    suspend fun getAuthorWorks(
        @Path("Id")
        id: String,
    ): AuthorWorksQueryResponse

}
