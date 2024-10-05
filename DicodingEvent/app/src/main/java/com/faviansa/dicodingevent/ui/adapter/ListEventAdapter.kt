package com.faviansa.dicodingevent.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faviansa.dicodingevent.R
import com.faviansa.dicodingevent.data.local.entity.EventEntity
import com.faviansa.dicodingevent.databinding.ItemCardBinding
import com.faviansa.dicodingevent.ui.MainViewModel
import com.faviansa.dicodingevent.utils.DateFormat.formatCardDate


class ListEventAdapter(
    private val onClickedItem: (EventEntity) -> Unit,
    private val viewType: Int,
    private val viewModel: MainViewModel
) : RecyclerView.Adapter<ListEventAdapter.MyListEventViewHolder>() {
    private val upcomingEvents = mutableListOf<EventEntity>()
    private val finishedEvents = mutableListOf<EventEntity>()
    private val favoriteEvents = mutableListOf<EventEntity>()

    inner class MyListEventViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(event: EventEntity) {
            with(binding) {
                eventName.text = event.name
                eventPlace.text = event.cityName
                eventDate.text = formatCardDate(event.beginTime)
                "${event.registrants}/${event.quota}".also { eventQuota.text = it }

                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(eventPhoto)

                if (event.isFavorite) {
                    btnFavorite.background =
                        itemView.context.getDrawable(R.drawable.baseline_favorite_24)
                } else {
                    btnFavorite.background =
                        itemView.context.getDrawable(R.drawable.baseline_favorite_border_24)
                }

                btnFavorite.setOnClickListener {
                    viewModel.setFavoriteEventState(event, !event.isFavorite)
                    btnFavorite.background = if (event.isFavorite) {
                        itemView.context.getDrawable(R.drawable.baseline_favorite_24)
                    } else {
                        itemView.context.getDrawable(R.drawable.baseline_favorite_border_24)
                    }

                    val toastMessage =
                        if (event.isFavorite) "Event added to favorite" else "Event removed from favorite"
                    Toast.makeText(itemView.context, toastMessage, Toast.LENGTH_SHORT).show()
                }


                itemView.setOnClickListener { onClickedItem(event) }
            }
        }
    }

    fun setUpcomingEvents(newEvents: List<EventEntity>) {
        val diffCallback = EventDiffCallback(upcomingEvents, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        upcomingEvents.clear()
        upcomingEvents.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
        Log.d("ListEventAdapter", "Upcoming events updated: $newEvents")
    }

    fun setFinishedEvents(newEvents: List<EventEntity>) {
        val diffCallback = EventDiffCallback(finishedEvents, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        finishedEvents.clear()
        finishedEvents.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
    }

    fun setFavoriteEvents(newEvents: List<EventEntity>) {
        val diffCallback = EventDiffCallback(favoriteEvents, newEvents)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        favoriteEvents.clear()
        favoriteEvents.addAll(newEvents)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListEventViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyListEventViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return when (viewType) {
            UPCOMING_VIEW_TYPE -> upcomingEvents.size
            FINISHED_VIEW_TYPE -> finishedEvents.size
            FAVORITE_VIEW_TYPE -> favoriteEvents.size
            else -> 0
        }
    }

    override fun onBindViewHolder(holder: MyListEventViewHolder, position: Int) {
        val event = when (viewType) {
            UPCOMING_VIEW_TYPE -> upcomingEvents[position]
            FINISHED_VIEW_TYPE -> finishedEvents[position]
            FAVORITE_VIEW_TYPE -> favoriteEvents[position]
            else -> throw IllegalArgumentException("Invalid view type")
        }
        holder.bind(event)
    }

    private class EventDiffCallback(
        private val oldEventsList: List<EventEntity>,
        private val newEventsList: List<EventEntity>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldEventsList.size
        override fun getNewListSize(): Int = newEventsList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldEventsList[oldItemPosition].id == newEventsList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldEventsList[oldItemPosition] == newEventsList[newItemPosition]
        }
    }

    companion object {
        const val UPCOMING_VIEW_TYPE = 1
        const val FINISHED_VIEW_TYPE = 2
        const val FAVORITE_VIEW_TYPE = 3
    }
}