package com.ant.podplay.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ant.podplay.R
import com.ant.podplay.databinding.FragmentPodcastDetailsBinding
import com.ant.podplay.viewmodel.PodcastViewModel
import com.bumptech.glide.Glide

class PodcastDetailsFragment : Fragment() {
    private lateinit var databinding: FragmentPodcastDetailsBinding
    private val podcastViewModel: PodcastViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
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

    // Super call onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateControls()
    }

    // Inflate the menu details
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_details, menu)
    }

    // Update the controls
    private fun updateControls() {

        // Check the view data to make sure it's available; return if it is null
        val viewData = podcastViewModel.activePodcastViewData ?: return

        // Populate the title and description
        databinding.feedTitleTextView.text = viewData.feedTitle
        databinding.feedDescTextView.text = viewData.feedDesc

        // Load the podcast image
        activity?.let { activity ->
            Glide.with(activity).load(viewData.imageUrl).into(databinding.feedImageView)
        }
    }

    // Return an instance of PodcastDetailsFragment
    companion object {
        fun newInstance(): PodcastDetailsFragment {
            return PodcastDetailsFragment()
        }
    }
}