package com.faviansa.dicodingevent.ui.finished

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
import com.faviansa.dicodingevent.data.Result
import com.faviansa.dicodingevent.databinding.FragmentFinishedBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.ViewModelFactory
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private lateinit var finishedAdapter: ListEventAdapter
    private lateinit var finishedRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        setupRecyclerView()
        observeFinishedEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        finishedRv = binding.rvFinished

        finishedAdapter = ListEventAdapter(
            onClickedItem = {
                val action = FinishedFragmentDirections.actionFinishedFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.FINISHED_VIEW_TYPE,
            viewModel = viewModel
        )

        finishedRv.apply {
            adapter = finishedAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeFinishedEvents() {
        viewModel.getFinishedEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Log.d("Finished Fragment", "Upcoming events: ${result.data}")
                    finishedAdapter.setFinishedEvents(result.data)
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("Finished Fragment", "Error: ${result.error}")
                }
            }
        }
    }

//    private fun showErrorDialog(message: String) {
//        val hasData = mainViewModel.finishedEvents.value != null
//
//        val builder = AlertDialog.Builder(requireContext())
//            .setTitle("Error")
//            .setMessage(message)
//            .setPositiveButton("Retry") { dialog, _ ->
//                mainViewModel.getAllFinishedEvents()
//                dialog.dismiss()
//            }
//            .setCancelable(hasData)
//
//        if (hasData) {
//            builder.setNegativeButton("Close") { dialog, _ ->
//                dialog.dismiss()
//            }
//        } else {
//            builder.setNegativeButton("Close") { _, _ ->
//                Toast.makeText(context, "Please turn on your internet connection before closing", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        val dialog = builder.create()
//
//        dialog.setOnShowListener {
//            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
//            negativeButton.isEnabled = hasData
//        }
//
//        dialog.show()
//    }
}