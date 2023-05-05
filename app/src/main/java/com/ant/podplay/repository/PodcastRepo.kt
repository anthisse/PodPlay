package com.ant.podplay.repository

import com.ant.podplay.model.Episode
import com.ant.podplay.model.Podcast
import com.ant.podplay.service.RssFeedResponse
import com.ant.podplay.service.RssFeedService
import com.ant.podplay.util.DateUtils

class PodcastRepo(private var feedService: RssFeedService) {

    // Get a podcast
    suspend fun getPodcast(feedUrl: String): Podcast? {
        var podcast: Podcast? = null

        // Get the feedResponse
        val feedResponse = feedService.getFeed(feedUrl)

        // If we got a feedResponse
        if (feedResponse != null) {
            // Convert podcast to a Podcast object
            podcast = rssResponseToPodcast(feedUrl, "", feedResponse)
        }
        // If the feedResponse is null, return null, otherwise return the updated podcast
        return podcast
    }

    // Convert RSS items to podcast episodes
    private fun rssItemsToEpisodes(episodeResponses: List<RssFeedResponse.EpisodeResponse>):
            List<Episode> {

        // Return a list of Episode objects
        return episodeResponses.map {
            Episode(
                it.guid ?: "",
                it.title ?: "",
                it.description ?: "",
                it.url ?: "",
                it.type ?: "",
                DateUtils.xmlDateToDate(it.pubDate),
                it.duration ?: ""
            )
        }
    }

    private fun rssResponseToPodcast(
        feedUrl: String, imageUrl: String, rssResponse: RssFeedResponse
    ): Podcast? {

        // Assign the list of episodes to items
        val items = rssResponse.episodes ?: return null

        // If there's an empty description, then set the description to the response summary
        val description = if (rssResponse.description == "") {
            rssResponse.summary
        } else {
            // Otherwise set the description to the response's description
            rssResponse.description
        }

        // Return a Podcast object
        return Podcast(
            feedUrl, rssResponse.title, description, imageUrl,
            rssResponse.lastUpdated, episodes = rssItemsToEpisodes(items)
        )
    }
}