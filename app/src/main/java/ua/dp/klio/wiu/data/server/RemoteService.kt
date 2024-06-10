package ua.dp.klio.wiu.data.server

import retrofit2.http.GET
import retrofit2.http.Query
import ua.dp.klio.wiu.domain.Movie

interface RemoteService {
    @GET("lastmovies")
    suspend fun listLastMovies(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("lastcartoons")
    suspend fun listLastCartoons(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("lasttvs")
    suspend fun listLastTVs(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("lastanimes")
    suspend fun listLastAnimes(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("getepisodes")
    suspend fun listEpisodesTVs(
        @Query("tvId") tvId: Int,
        @Query("seasonNum") seasonNum: Int
    ): RemoteResult

    @GET("lastfromklio")
    suspend fun listLastFromKlio(
        @Query("page") page: Int = 1
    ): RemoteResult

    @GET("getkliopartition")
    suspend fun listKlioMovies(
        @Query("partitionId") partitionId: Int
    ): RemoteResult
}