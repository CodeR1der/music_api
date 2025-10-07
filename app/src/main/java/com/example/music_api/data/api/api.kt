package com.example.music_api.data.api

import com.example.music_api.data.model.ArtistInfoResponse
import com.example.music_api.data.model.TopTracksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LastFmApi {

    @GET("?method=artist.getinfo&format=json")
    suspend fun getArtistInfo(
        @Query("artist") artistName: String,
        @Query("api_key") apiKey: String,
        @Query("lang") language: String = "ru",
        @Query("autocorrect") autoCorrect: Int = 1
    ): Response<ArtistInfoResponse>

    @GET("?method=artist.gettoptracks&format=json&limit=10")
    suspend fun getTopTracks(
        @Query("artist") artistName: String,
        @Query("api_key") apiKey: String,
        @Query("autocorrect") autoCorrect: Int = 1
    ): Response<TopTracksResponse>
}