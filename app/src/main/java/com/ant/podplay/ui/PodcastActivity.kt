package com.ant.podplay.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.ant.podplay.R
import com.ant.podplay.repository.ItunesRepo
import com.ant.podplay.service.ItunesService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PodcastActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_podcast)

        val itunesService = ItunesService.instance
        val itunesRepo = ItunesRepo(itunesService)

        GlobalScope.launch {
            val results = itunesRepo.searchByTerm("Android Developer")
            Log.i(TAG, "Results = ${results.body()}")
        }
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
}