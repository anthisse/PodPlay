package com.ant.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ant.podplay.model.Episode
import com.ant.podplay.model.Podcast
import com.ant.podplay.repository.PodcastRepo
import java.util.*

class PodcastViewModel(application: Application) : AndroidViewModel(application) {

    var podcastRepo: PodcastRepo? = null
    var activePodcastViewData: PodcastViewData? = null

    data class PodcastViewData(
        var subscribed: Boolean = false,
        var feedTitle: String? = "",
        var feedUrl: String? = "",
        var feedDesc: String? = "",
        var imageUrl: String? = "",
        var episodes: List<EpisodeViewData>
    )

    data class EpisodeViewData(
        var guid: String? = "",
        var title: String? = "",
        var description: String? = "",
        var mediaUrl: String? = "",
        var releaseDate: Date? = null,
        var duration: String? = ""
    )


    // Iterate over episodes, convert them to EpisodeViewDatas, and put them in a list
    private fun episodesToEpisodesView(episodes: List<Episode>): List<EpisodeViewData> {
        return episodes.map {
            EpisodeViewData(
                it.guid,
                it.title,
                it.description,
                it.mediaUrl,
                it.releaseDate,
                it.duration
            )
        }
    }

    // Convert from Podcast models to PodcastViewDatas

    private fun podcastToPodcastView(podcast: Podcast): PodcastViewData {
        return PodcastViewData(
            false,
            podcast.feedTitle,
            podcast.feedUrl,
            podcast.feedDesc,
            podcast.imageUrl,
            episodesToEpisodesView(podcast.episodes)
        )
    }

    // Get a podcast from the repo
    fun getPodcast(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData)
    : PodcastViewData? {

        val repo = podcastRepo ?: return null
        val feedUrl = podcastSummaryViewData.feedUrl ?: return null
        val podcast = repo.getPodcast(feedUrl)

        // Set the podcast's attributes
        podcast?.let {
            it.feedTitle = podcastSummaryViewData.name ?: ""
            it.imageUrl = podcastSummaryViewData.imageUrl ?: ""

            // Convert the Podcast to a PodcastViewData and return it
            activePodcastViewData = podcastToPodcastView(it)
            return activePodcastViewData
        }

        // Return null if no podcast was retrieved
        return null
    }
}