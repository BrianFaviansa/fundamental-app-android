package com.faviansa.dicodingevent.ui.upcoming

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
import com.faviansa.dicodingevent.data.remote.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.FragmentUpcomingBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel =
            ViewModelProvider(requireActivity())[MainViewModel::class.java]

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()

        if (mainViewModel.upcomingEvents.value == null) {
            mainViewModel.getAllUpcomingEvents()
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        binding.rvUpcoming.layoutManager = layoutManager
        binding.rvUpcoming.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
    }

    private fun setupSearchView() {
        binding.svUpcoming.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { mainViewModel.searchUpcomingEvents(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { mainViewModel.searchUpcomingEvents(it) }
                return true
            }
        })
    }

    private fun observeViewModel() {
        mainViewModel.upcomingEvents.observe(viewLifecycleOwner) { listEvents ->
            listEvents?.let { setEventsData(it) }
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

    private fun setEventsData(listEvents: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(listEvents)
        binding.rvUpcoming.adapter = adapter
    }

    private fun showErrorDialog(message: String) {
        val hasData = mainViewModel.upcomingEvents.value != null

        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Retry") { dialog, _ ->
                mainViewModel.getAllUpcomingEvents()
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