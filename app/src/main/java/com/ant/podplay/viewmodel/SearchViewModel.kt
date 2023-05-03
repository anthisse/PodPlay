package com.ant.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ant.podplay.repository.ItunesRepo
import com.ant.podplay.service.PodcastResponse

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    var iTunesRepo: ItunesRepo? = null

    // Data class for the View
    data class PodcastSummaryViewData(
        var name: String? = "",
        var lastUpdated: String? = "",
        var imageUrl: String? = "",
        var feedUrl: String? = ""
    )

    // Convert from raw model data to view data
    private fun itunesPodcastToPodcastSummaryView(
        itunesPodcast: PodcastResponse.ItunesPodcast
    ): PodcastSummaryViewData {

        // Return view data
        return PodcastSummaryViewData(
            itunesPodcast.collectionCensoredName,
            itunesPodcast.releaseDate,
            itunesPodcast.artworkUrl30,
            itunesPodcast.feedUrl
        )
    }

    // Search for a podcast
    suspend fun searchPodcasts(term: String):
            List<PodcastSummaryViewData> {
        // Search by term
        val results = iTunesRepo?.searchByTerm(term)

        // If the results were successfully found and were not null
        if (results != null && results.isSuccessful) {
            // Get the podcasts
            val podcasts = results.body()?.results

            // If the podcast list is not empty
            if (!podcasts.isNullOrEmpty()) {
                // Map them to PodcastSummaryViewData objects and return them
                return podcasts.map { podcast -> itunesPodcastToPodcastSummaryView(podcast) }
            }
        }
        // Just return an empty list if the results are null
        return emptyList()
    }
}

