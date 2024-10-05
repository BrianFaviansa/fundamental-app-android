package com.faviansa.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.faviansa.dicodingevent.data.Result
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.databinding.FragmentDetailBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.ViewModelFactory
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore
import com.faviansa.dicodingevent.utils.DateFormat.formatCardDate
import com.faviansa.dicodingevent.utils.DateFormat.formatDetailTime

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private val eventId by lazy { args.eventId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Event Detail" // or dynamically set based on the event
        }

        @Suppress("DEPRECATION")
        setHasOptionsMenu(true)

        observeEventDetail()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeEventDetail() {
        viewModel.getEventById(eventId).observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.VISIBLE
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val event = result.data
                    bindData(event)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("Detail Fragment", "observeEventDetail: ${result.error}")
                }
            }

        }
    }

    private fun bindData(event: EventEntity) {
        binding.apply {
            detailEventOwner.text = event.ownerName
            detailEventName.text = event.name
            detailEventPlace.text = event.cityName
            detailEventDate.text = formatCardDate(event.beginTime)
            detailEventBeginTime.text = formatDetailTime(event.beginTime)
            detailEventQuota.text = "${event.quota - event.registrants}"
            detailEventDescription.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            Glide.with(requireContext())
                .load(event.mediaCover)
                .into(detailEventImage)
            btnEventLink.setOnClickListener {

                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
                val browserChooserIntent =
                    Intent.createChooser(browserIntent, "Choose browser to open")
                startActivity(browserChooserIntent)
            }
        }
    }

}