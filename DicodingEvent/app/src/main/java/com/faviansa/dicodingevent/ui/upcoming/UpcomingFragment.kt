package com.faviansa.dicodingevent.ui.upcoming

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faviansa.dicodingevent.data.Result
import com.faviansa.dicodingevent.databinding.FragmentUpcomingBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.ViewModelFactory
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private lateinit var upcomingAdapter: ListEventAdapter
    private lateinit var upcomingRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        setupRecyclerView()
        setupSearchView()
        observeUpcomingEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        upcomingRv = binding.rvUpcoming

        upcomingAdapter = ListEventAdapter(
            onClickedItem = {
                val action =
                    UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.UPCOMING_VIEW_TYPE,
            viewModel = viewModel,
            lifecycleOwner = viewLifecycleOwner
        )

        upcomingRv.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupSearchView() {
        binding.svUpcoming.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchEvents(1, query).observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            upcomingAdapter.setUpcomingEvents(result.data)
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                upcomingAdapter.setUpcomingEvents(result.data)
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Log.e("Upcoming Fragment", "Error: ${result.error}")
                            }
                        }
                    }
                } else {
                    viewModel.searchEvents(1, newText).observe(viewLifecycleOwner) { result ->
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                upcomingAdapter.setUpcomingEvents(result.data)
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Log.e("Upcoming Fragment", "Error: ${result.error}")
                            }
                        }
                    }
                }
                return false
            }
        })
    }

    private fun observeUpcomingEvents() {
        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    upcomingAdapter.setUpcomingEvents(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    showErrorSnackbar(result.error)
                }
            }
        }
    }

    private fun showErrorSnackbar(errorMessage: String) {
        view?.let { view ->
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    observeUpcomingEvents()
                }
                .show()
        }
    }
}