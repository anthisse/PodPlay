package com.ant.podplay.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ant.podplay.model.Episode
import com.ant.podplay.model.Podcast
import com.ant.podplay.repository.PodcastRepo
import kotlinx.coroutines.launch
import java.util.*

class PodcastViewModel(application: Application) : AndroidViewModel(application) {

    private val _podcastLiveData = MutableLiveData<PodcastViewData?>()
    val podcastLiveData: LiveData<PodcastViewData?> = _podcastLiveData
    var podcastRepo: PodcastRepo? = null

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
    fun getPodcast(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {
        // Use a coroutine to get a podcast from the repo
        podcastSummaryViewData.feedUrl?.let { url ->
            viewModelScope.launch {

                // If the attributes happen to be null, set them as empty strings
                podcastRepo?.getPodcast(url)?.let {
                    it.feedTitle = podcastSummaryViewData.name ?: ""
                    it.imageUrl = podcastSummaryViewData.imageUrl ?: ""
                    _podcastLiveData.value = podcastToPodcastView(it)

                    // If the whole podcast is null, just provide null for LiveData
                } ?: run {
                    _podcastLiveData.value = null
                }
            }

            // If the feedUrl is null, just provide null for LiveData
        } ?: run {
            _podcastLiveData.value = null
        }
    }
}