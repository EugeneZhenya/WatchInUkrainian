package ua.dp.klio.wiu.data.server

import retrofit2.http.GET
import retrofit2.http.Query

interface RemoteService {
    @GET("lastmovies")
    suspend fun listLastMovies(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("lastcartoons")
    suspend fun listLastCartoons(
        @Query("page") page: Int = 1
    ): RemoteResult
}