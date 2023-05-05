package com.ant.podplay.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ant.podplay.R
import com.ant.podplay.adapter.EpisodeListAdapter
import com.ant.podplay.databinding.FragmentPodcastDetailsBinding
import com.ant.podplay.viewmodel.PodcastViewModel
import com.bumptech.glide.Glide

class PodcastDetailsFragment : Fragment() {
    private lateinit var databinding: FragmentPodcastDetailsBinding
    private lateinit var episodeListAdapter: EpisodeListAdapter
    private val podcastViewModel: PodcastViewModel by activityViewModels()

    // When a PodcastDetailsFragment is created
    // Suppressed warning for deprecated member function setHasOptionsMenu
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {

        // Super call onCreate
        super.onCreate(savedInstanceState)

        // Add items to the options menu
        setHasOptionsMenu(true)
    }

    // Set databinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        databinding = FragmentPodcastDetailsBinding.inflate(inflater, container, false)
        return databinding.root
    }

    // When a View is created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // Super call onViewCreated
        super.onViewCreated(view, savedInstanceState)

        // As long as the fragment is active
        podcastViewModel.podcastLiveData.observe(viewLifecycleOwner) { viewData ->

            // As long as the view data isn't null, populate the feed's attributes from it
            if (viewData != null) {
                databinding.feedTitleTextView.text = viewData.feedTitle
                databinding.feedDescTextView.text = viewData.feedDesc

                // Use Glide to load view data in while scrolling
                activity?.let { activity ->
                    Glide.with(activity).load(viewData.imageUrl).into(databinding.feedImageView)
                }

                // Scroll the feed title if it's too long for the container
                databinding.feedDescTextView.movementMethod = ScrollingMovementMethod()

                // Set up the RecyclerView
                databinding.episodeRecyclerView.setHasFixedSize(true)
                val layoutManager = LinearLayoutManager(activity)
                databinding.episodeRecyclerView.layoutManager = layoutManager

                // Add a divider between items
                val dividerItemDecoration = DividerItemDecoration(
                    databinding.episodeRecyclerView.context,
                    layoutManager.orientation
                )
                databinding.episodeRecyclerView.addItemDecoration(dividerItemDecoration)

                // Create an EpisodeListAdapter with the list of episodes in activePodcastViewData
                episodeListAdapter = EpisodeListAdapter(viewData.episodes)
                databinding.episodeRecyclerView.adapter = episodeListAdapter
            }
        }
    }

    // Inflate the menu details
    // Suppress deprecated warning for onCreateOptionsMenu
    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_details, menu)
    }

    // Return an instance of PodcastDetailsFragment
    companion object {
        fun newInstance(): PodcastDetailsFragment {
            return PodcastDetailsFragment()
        }
    }
}