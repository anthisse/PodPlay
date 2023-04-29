package com.ant.podplay.service

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {

    // Define a path
    @GET("/search?media=podcast")

    // Add "term" as a query term in the path
    suspend fun searchPodcastByTerm(@Query("term") term: String):
            Response<PodcastResponse>

    // Define a companion object for the ItunesService interface
    companion object {

        // Hold the application-wide instance of ItunesService
        val instance: ItunesService by lazy {

            // Build a RetroFitBuilder object and set the base URL and convert between Java and JSON
            val retrofit = Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            // Create an instance of ItunesService
            retrofit.create(ItunesService::class.java)
        }
    }
}