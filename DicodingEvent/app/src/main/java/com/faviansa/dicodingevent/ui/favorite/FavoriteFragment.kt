package com.faviansa.dicodingevent.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faviansa.dicodingevent.databinding.FragmentFavoriteBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.ui.ViewModelFactory
import com.faviansa.dicodingevent.ui.adapter.ListEventAdapter
import com.faviansa.dicodingevent.ui.settings.SettingPreferences
import com.faviansa.dicodingevent.ui.settings.dataStore


class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferences: SettingPreferences
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity(), preferences)
    }
    private lateinit var favoriteAdapter: ListEventAdapter
    private lateinit var favoriteRv: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferences = SettingPreferences.getInstance(requireContext().applicationContext.dataStore)

        setupRecyclerView()
        observeFavoriteEvents()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupRecyclerView() {
        favoriteRv = binding.rvFavorite

        favoriteAdapter = ListEventAdapter(
            onClickedItem = {
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(it.id)
                findNavController().navigate(action)
            },
            viewType = ListEventAdapter.FAVORITE_VIEW_TYPE,
            viewModel = viewModel
        )

        favoriteRv.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeFavoriteEvents() {
        viewModel.getFavoriteEvents().observe(viewLifecycleOwner) { result ->
            favoriteAdapter.setFavoriteEvents(result)
        }
    }

}