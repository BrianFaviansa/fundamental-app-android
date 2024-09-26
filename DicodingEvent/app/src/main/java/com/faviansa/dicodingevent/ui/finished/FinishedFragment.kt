package com.faviansa.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.FragmentFinishedBinding
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var finishedViewModel: FinishedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        finishedViewModel =
            ViewModelProvider(this).get(FinishedViewModel::class.java)

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        binding.rvFinished.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        binding.rvFinished.addItemDecoration(itemDecoration)

        finishedViewModel.finishedEvents.observe(viewLifecycleOwner) { listEvents ->
            setEventsData(listEvents)
        }

        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setEventsData(listEvents: List<ListEventsItem>) {
        val adapter = ListEventAdapter()
        adapter.submitList(listEvents)
        binding.rvFinished.adapter = adapter
    }
}