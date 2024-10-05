package com.faviansa.dicodingevent.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faviansa.dicodingevent.data.Result.Error
import com.faviansa.dicodingevent.data.Result.Loading
import com.faviansa.dicodingevent.data.Result.Success
import com.faviansa.dicodingevent.databinding.FragmentHomeBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.ViewModelFactory
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private lateinit var upcomingAdapter: ListEventAdapter
    private lateinit var finishedAdapter: ListEventAdapter
    private lateinit var upcomingRv: RecyclerView
    private lateinit var finishedRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        setupRecyclerViews()
        observeHomeEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerViews() {
        upcomingRv = binding.rvUpcoming
        finishedRv = binding.rvFinished

        upcomingAdapter = ListEventAdapter(
            onClickedItem = {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.UPCOMING_VIEW_TYPE,
            viewModel = viewModel
        )

        finishedAdapter = ListEventAdapter(
            onClickedItem = {
                val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.FINISHED_VIEW_TYPE,
            viewModel = viewModel
        )

        upcomingRv.adapter = upcomingAdapter
        upcomingRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        finishedRv.adapter = finishedAdapter
        finishedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun observeHomeEvents() {
        viewModel.getUpcomingEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("HomeFragment", "Upcoming events: ${result.data}")
                        upcomingAdapter.setUpcomingEvents(result.data.take(5))
                    }

                    is Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", "Error: ${result.error})}")
                    }
                }
            }
        }
        viewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        Log.d("HomeFragment", "Finished events: ${result.data}")
                        finishedAdapter.setFinishedEvents(result.data.take(5))
                    }

                    is Error -> {
                        binding.progressBar.visibility = View.GONE
                        Log.e("HomeFragment", "Error: ${result.error}")
                    }
                }
            }
        }
    }
}

