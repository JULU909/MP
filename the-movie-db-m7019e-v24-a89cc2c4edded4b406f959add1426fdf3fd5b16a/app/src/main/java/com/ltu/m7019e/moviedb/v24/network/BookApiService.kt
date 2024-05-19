package com.ltu.m7019e.moviedb.v24.network

import com.ltu.m7019e.moviedb.v24.model.TrendingWorksQueryResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query




interface BookApiService {
//    @GET("games")
//    suspend fun getGames(
//        @Query("key")
//        key: String = SECRETS.RAWG_API_KEY
//    ) : GameResponse

    @GET("trending/{Time}.json")
    suspend fun getTrendingWorks(
        @Path("Time")
        time: String = "weekly",
        @Query("limit")
        limit: String = "10",
    ): TrendingWorksQueryResponse
}
