package com.faviansa.dicodingevent.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.FragmentHomeBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel =
            ViewModelProvider(requireActivity())[MainViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        observeViewModel()

        if (savedInstanceState == null) {
            mainViewModel.getHomeEvents()
        }
    }

    private fun setupRecyclerViews() {
        val layoutManagerFinished = LinearLayoutManager(context)
        binding.rvFinished.layoutManager = layoutManagerFinished
        binding.rvFinished.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManagerFinished.orientation
            )
        )

        val layoutManagerUpcoming =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcoming.layoutManager = layoutManagerUpcoming
        binding.rvUpcoming.addItemDecoration(
            DividerItemDecoration(
                context,
                layoutManagerUpcoming.orientation
            )
        )
    }

    private fun observeViewModel() {
        mainViewModel.homeUpcomingEvents.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let { setUpcomingEventsData(it) }
        }

        mainViewModel.homeFinishedEvents.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let { setFinishedEventsData(it) }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                if (!mainViewModel.isErrorHandled()) {
                    showErrorDialog(it)
                    mainViewModel.errorHandled()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpcomingEventsData(listEvents: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(listEvents)
        binding.rvUpcoming.adapter = adapter
    }

    private fun setFinishedEventsData(listEvents: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(listEvents)
        binding.rvFinished.adapter = adapter
    }

    private fun showErrorDialog(message: String) {
        val hasData = mainViewModel.homeFinishedEvents.value != null &&
                mainViewModel.homeUpcomingEvents.value != null

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Retry") { dialog, _ ->
                mainViewModel.getHomeEvents()
                dialog.dismiss()
            }
            .setCancelable(hasData)

        if (hasData) {
            builder.setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
        } else {
            builder.setNegativeButton("Close") { _, _ ->
                Toast.makeText(context, "Please turn on your internet connection before closing", Toast.LENGTH_SHORT).show()
            }
        }

        val dialog = builder.create()

        dialog.setOnShowListener {
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            negativeButton.isEnabled = hasData
        }

        dialog.show()
    }
}