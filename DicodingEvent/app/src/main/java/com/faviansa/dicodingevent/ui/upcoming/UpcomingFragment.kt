package com.faviansa.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.FragmentUpcomingBinding
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter

class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingViewModel: UpcomingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        upcomingViewModel =
            ViewModelProvider(this).get(UpcomingViewModel::class.java)

        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvUpcoming.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvUpcoming.addItemDecoration(itemDecoration)

        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner) { listEvents ->
            setEventsData(listEvents)
        }

        // Optionally observe loading and error states
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        upcomingViewModel.error.observe(viewLifecycleOwner) { error ->

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setEventsData(listEvents: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(listEvents)
        binding.rvUpcoming.adapter = adapter
    }
}