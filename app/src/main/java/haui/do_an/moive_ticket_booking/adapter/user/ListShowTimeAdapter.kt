package haui.do_an.moive_ticket_booking.adapter.user

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.databinding.ItemShowtimeUserBinding
import haui.do_an.moive_ticket_booking.view.user.UserActivity

class ListShowTimeAdapter(
    private val context: Context,
    private var showTimeList: List<ShowTimeDTO>,
) : ListAdapter<String, ListShowTimeAdapter.ShowTimeViewHolder>(ShowTimeDiffCallback()) {

    private var selectedTime = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowTimeViewHolder {
        val binding = ItemShowtimeUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class ShowTimeViewHolder(private val binding: ItemShowtimeUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(name: String) {
            binding.tvNameCinema.text = name

            val adapter = ListTimeAdapter { showTime ->
                val bundle = Bundle()
                bundle.putInt("showtimeId", showTime.id)
                bundle.putInt("hallId", showTime.hallId)
                bundle.putDouble("price", showTime.ticketPrice!!)
                (context as UserActivity).navigateToSelectSeat(bundle)
                Log.d("showtimeId", showTime.id.toString())
            }

            binding.rcTime.adapter = adapter
            val listTime = showTimeList.filter { it.cinemaName == name }
            adapter.submitList(listTime)

            binding.clickableLayout.setOnClickListener {
                if (!selectedTime){
                    binding.lnTime.visibility = View.VISIBLE
                    selectedTime = true
                }else{
                    binding.lnTime.visibility = View.GONE
                    selectedTime = false
                }
            }
        }
    }

    class ShowTimeDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    fun updateList(newList: List<ShowTimeDTO>) {
        showTimeList = newList
        notifyDataSetChanged()
    }

}