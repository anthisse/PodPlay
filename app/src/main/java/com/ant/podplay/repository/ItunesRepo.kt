package com.ant.podplay.repository

import com.ant.podplay.service.ItunesService


class ItunesRepo(private val itunesService: ItunesService) {

    // Search for a term by calling searchPodcastByTerm and passing it a term
    suspend fun searchByTerm(term: String) = itunesService.searchPodcastByTerm(term)
}