package com.faviansa.dicodingevent.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.faviansa.dicodingevent.databinding.FragmentFinishedBinding
import com.faviansa.dicodingevent.ui.MainViewModel

class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        mainViewModel =
//            ViewModelProvider(requireActivity())[MainViewModel::class.java]

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        setupRecyclerView()
//        setupSearchView()
//        observeViewModel()
//
//        if (mainViewModel.finishedEvents.value == null) {
//            mainViewModel.getAllFinishedEvents()
//        }
    }

//    private fun setupRecyclerView() {
//        val layoutManager = LinearLayoutManager(context)
//        binding.rvFinished.layoutManager = layoutManager
//        binding.rvFinished.addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
//    }
//
//    private fun setupSearchView() {
//        binding.svFinished.setOnQueryTextListener(object :
//            androidx.appcompat.widget.SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                query?.let { mainViewModel.searchFinishedEvents(it) }
//                return true
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                newText?.let { mainViewModel.searchFinishedEvents(it) }
//                return true
//            }
//        })
//    }
//
//    private fun observeViewModel() {
//        mainViewModel.finishedEvents.observe(viewLifecycleOwner) { listEvents ->
//            listEvents?.let { setEventsData(it) }
//        }
//
//        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
//        }
//
//        mainViewModel.error.observe(viewLifecycleOwner) { error ->
//            error?.let {
//                if (!mainViewModel.isErrorHandled()) {
//                    showErrorDialog(it)
//                    mainViewModel.errorHandled()
//                }
//            }
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun setEventsData(listEvents: List<ListEventsItem>) {
//        val adapter = ListEventAdapter()
//        adapter.submitList(listEvents)
//        binding.rvFinished.adapter = adapter
//    }
//
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