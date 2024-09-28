package com.faviansa.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.FragmentDetailBinding
import com.faviansa.dicodingevent.utils.DateFormat.formatCardDate
import com.faviansa.dicodingevent.utils.DateFormat.formatDetailTime


class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()

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

        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Event Detail" // or dynamically set based on the event
        }

        setHasOptionsMenu(true)

        val eventId = args.eventId

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.event.observe(viewLifecycleOwner) { event: ListEventsItem? ->
            event?.let { it ->
                binding.detailEventName.text = it.name
                binding.detailEventOwner.text = it.ownerName
                binding.detailEventDate.text = it.beginTime?.let { formatCardDate(it) }
                binding.detailEventBeginTime.text = it.beginTime?.let { formatDetailTime(it) }
                "${it.registrants}/${it.quota}".also { binding.detailEventQuota.text = it }
                binding.detailEventDescription.text = HtmlCompat.fromHtml(
                    it.description.toString(),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
                Glide.with(this).load(it.imageLogo).into(binding.detailEventImage)
            }
        }

        binding.btnEventLink.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.event.value?.link))
            val browserChooserIntent =
                Intent.createChooser(browserIntent, "Choose browser to open")
            startActivity(browserChooserIntent)
        }

        viewModel.getDetailEvent(eventId)
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

}