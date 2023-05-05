package com.ant.podplay.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ant.podplay.R
import com.ant.podplay.adapter.PodcastListAdapter
import com.ant.podplay.databinding.ActivityPodcastBinding
import com.ant.podplay.repository.ItunesRepo
import com.ant.podplay.repository.PodcastRepo
import com.ant.podplay.service.ItunesService
import com.ant.podplay.viewmodel.PodcastViewModel
import com.ant.podplay.viewmodel.SearchViewModel
import kotlinx.coroutines.*

class PodcastActivity : AppCompatActivity(), PodcastListAdapter.PodcastListAdapterListener {
    private val searchViewModel by viewModels<SearchViewModel>()
    private lateinit var databinding: ActivityPodcastBinding
    private lateinit var podcastListAdapter: PodcastListAdapter
    private lateinit var searchMenuItem: MenuItem
    private val podcastViewModel by viewModels<PodcastViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = ActivityPodcastBinding.inflate(layoutInflater)
        setContentView(databinding.root)

        // Set up the search toolbar
        setupToolbar()

        // Set up the view models
        setupViewModels()

        // Set up the controls
        updateControls()

        // Handle intents
        handleIntent(intent)

        // Handle a back button press
        addBackStackListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the options menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        // FIXME: Nullable call
        // Find the search item and set a search view from the item's actionView property
        searchMenuItem = menu?.findItem(R.id.search_item)!!
        val searchView = searchMenuItem.actionView as SearchView

        // Load the SearchManager
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        // Load the search configuration and assign it to the searchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        if (supportFragmentManager.backStackEntryCount > 0) {
            databinding.podcastRecyclerView.visibility = View.INVISIBLE
        }

        if (databinding.podcastRecyclerView.visibility == View.INVISIBLE) {
            searchMenuItem.isVisible = false
        }
        return true
    }

    // Handle a new Intent
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    // Show details
    override fun onShowDetails(podcastSummaryViewData: SearchViewModel.PodcastSummaryViewData) {

        // Get the feedUrl; return if null
        val feedUrl = podcastSummaryViewData.feedUrl ?: return

        // Show the progress bar
        showProgressBar()

        // Get the podcast view data
        val podcast = podcastViewModel.getPodcast(podcastSummaryViewData)

        // Hide the progress bar
        hideProgressBar()

        // If the podcast isn't null
        if (podcast != null) {
            // Show the Details Fragment
            showDetailsFragment()

        } else {
            // Otherwise, show an error
            showError("Error loading feed $feedUrl!")
        }
    }

    // Search for a podcast
    @OptIn(DelicateCoroutinesApi::class)
    private fun performSearch(term: String) {
        showProgressBar()
        GlobalScope.launch {
            val results = searchViewModel.searchPodcasts(term)
            withContext(Dispatchers.Main) {
                hideProgressBar()
                databinding.toolbar.title = term
                podcastListAdapter.setSearchData(results)
            }
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
        setSupportActionBar(databinding.toolbar)
    }

    // Set up the ViewModels
    private fun setupViewModels() {
        // Create an instance of the ItunesService
        val service = ItunesService.instance

        // Create an ItunesRepo object and assign it to the SearchViewModel
        searchViewModel.iTunesRepo = ItunesRepo(service)

        // Create a new instance of PodcastRepo
        podcastViewModel.podcastRepo = PodcastRepo()
    }

    // Set the controls for the activity
    private fun updateControls() {
        databinding.podcastRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        databinding.podcastRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            databinding.podcastRecyclerView.context, layoutManager.orientation
        )

        databinding.podcastRecyclerView.addItemDecoration(dividerItemDecoration)

        podcastListAdapter = PodcastListAdapter(null, this, this)
        databinding.podcastRecyclerView.adapter = podcastListAdapter
    }

    // Show a progress bar
    private fun showProgressBar() {
        databinding.progressBar.visibility = View.VISIBLE
    }

    // Hide a progress bar
    private fun hideProgressBar() {
        databinding.progressBar.visibility = View.INVISIBLE
    }

    // Create a PodcastDetailsFragment
    private fun createPodcastDetailsFragment(): PodcastDetailsFragment {

        // Check if the fragment exists
        var podcastDetailsFragment =
            supportFragmentManager.findFragmentByTag(TAG_DETAILS_FRAGMENT) as
                    PodcastDetailsFragment?

        // Create a new fragment
        if (podcastDetailsFragment == null) {
            podcastDetailsFragment = PodcastDetailsFragment.newInstance()
        }

        // Return the Fragment
        return podcastDetailsFragment
    }

    // Show the details fragments
    private fun showDetailsFragment() {
        val podcastDetailsFragment = createPodcastDetailsFragment()

        // Add the fragment to the supportFragmentManager
        supportFragmentManager.beginTransaction().add(
            R.id.podcastDetailsContainer,
            podcastDetailsFragment, TAG_DETAILS_FRAGMENT
        ).addToBackStack("DetailsFragment").commit()

        // Hide the main podcast RecyclerView
        databinding.podcastRecyclerView.visibility = View.INVISIBLE

        // Hide the search icon in the details screen
        searchMenuItem.isVisible = false
    }

    // Show an error message
    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok_button), null)
            .create()
            .show()
    }

    private fun addBackStackListener() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                databinding.podcastRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        private const val TAG_DETAILS_FRAGMENT = "DetailsFragment"
    }
}