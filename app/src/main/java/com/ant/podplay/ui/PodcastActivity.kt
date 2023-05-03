package com.ant.podplay.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.ant.podplay.R
import com.ant.podplay.databinding.ActivityPodcastBinding
import com.ant.podplay.repository.ItunesRepo
import com.ant.podplay.service.ItunesService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityPodcastBinding

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the search toolbar
        setupToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        // Replaced with safe call
        // Find the search item and set a search view from the item's actionView property
        val searchMenuItem = menu?.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView as SearchView

        // Load the SearchManager
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // Load the search configuration and assign it to the searchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    // Search for a term
    @OptIn(DelicateCoroutinesApi::class)
    private fun performSearch(term: String) {
        val itunesService = ItunesService.instance
        val itunesRepo = ItunesRepo(itunesService)

        GlobalScope.launch {
            val results = itunesRepo.searchByTerm(term)
            Log.i(TAG, "Results = ${results.body()}")
        }
    }

    // Handle incoming intents
    // Replaced with null safety
    private fun handleIntent(intent: Intent?) {
        // If the intent is an ACTION_SEARCH
        if (Intent.ACTION_SEARCH == intent?.action) {
            // Extract the search query and pass it to performSearch()
            val query = intent.getStringExtra(SearchManager.QUERY) ?: return
            performSearch(query)
        }
    }

    // Set up the search toolbar
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}