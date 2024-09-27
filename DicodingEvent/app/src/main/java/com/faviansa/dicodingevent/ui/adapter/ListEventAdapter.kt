package com.faviansa.dicodingevent.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faviansa.dicodingevent.R
import com.faviansa.dicodingevent.data.response.ListEventsItem
import com.faviansa.dicodingevent.databinding.ItemCardBinding
import com.faviansa.dicodingevent.ui.finished.FinishedFragmentDirections
import com.faviansa.dicodingevent.ui.home.HomeFragmentDirections
import com.faviansa.dicodingevent.ui.upcoming.UpcomingFragmentDirections
import com.faviansa.dicodingevent.utils.DateFormat.formatCardDate

class ListEventAdapter : ListAdapter<ListEventsItem, ListEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)

        holder.itemView.setOnClickListener { it ->
            val navController = it.findNavController()
            val action = when (navController.currentDestination?.id) {
                R.id.navigation_home -> HomeFragmentDirections.actionHomeFragmentToDetailFragment(
                    event.id.toString()
                )

                R.id.navigation_upcoming -> UpcomingFragmentDirections.actionUpcomingFragmentToDetailFragment(
                    event.id.toString()
                )

                R.id.navigation_finished -> FinishedFragmentDirections.actionFinishedFragmentToDetailFragment(
                    event.id.toString()
                )

                else -> null
            }
            action?.let { navController.navigate(it) }
        }
    }

    class MyViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            binding.eventName.text = event.name
            binding.eventPlace.text = event.cityName
            binding.eventDate.text = event.beginTime?.let { formatCardDate(it) }
            "${event.registrants}/${event.quota}".also { binding.eventQuota.text = it }

            Glide.with(binding.root.context)
                .load(event.imageLogo)
                .into(binding.eventPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}