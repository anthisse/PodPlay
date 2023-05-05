package com.ant.podplay.repository

import com.ant.podplay.model.Podcast
import com.ant.podplay.service.RssFeedResponse

class PodcastRepo {

    // Get a podcast
    fun getPodcast(feedUrl: String): Podcast {
        val rssFeedResponse = RssFeedResponse.instance
        rssFeedResponse.getFeed(feedUrl) {

        }
        return Podcast(feedUrl, "No Name", "No description", "No image")
    }
}