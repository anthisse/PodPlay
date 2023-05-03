package com.ant.podplay.repository

import com.ant.podplay.model.Podcast

class PodcastRepo {
    fun getPodcast(feedUrl: String): Podcast? {
        return Podcast(feedUrl, "No Name", "No description", "No image")
    }
}