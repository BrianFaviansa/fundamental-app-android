package com.faviansa.dicodingevent.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    inner class MyListEventViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            with(binding) {
                eventName.text = event.name
                eventPlace.text = event.cityName
                eventDate.text = formatCardDate(event.beginTime)
                "${event.registrants}/${event.quota}".also { eventQuota.text = it }

                Glide.with(itemView.context)
                    .load(event.imageLogo)
                    .into(eventPhoto)

//                btnFavorite.setOnClickListener {
//                    viewModel.setFavoriteEvent(event, !event.isFavorite)
//                }
//                btnFavorite.setImageDrawable(
//                    if (event.isFavorite) {
//                        itemView.resources.getDrawable(R.drawable.ic_favorite)
//                    } else {
//                        itemView.resources.getDrawable(R.drawable.ic_favorite_border)
//                    }
//                )
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
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: MyListEventViewHolder, position: Int) {
        TODO("Not yet implemented")
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
        private const val UPCOMING_VIEW_TYPE = 1
        private const val FINISHED_VIEW_TYPE = 2
        private const val FAVORITE_VIEW_TYPE = 3
    }
}