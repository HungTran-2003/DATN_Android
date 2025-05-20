package haui.do_an.moive_ticket_booking.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.databinding.ItemTimeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListTimeAdapter (
    private val onItemClick: (ShowTimeDTO) -> Unit
) :ListAdapter<ShowTimeDTO, ListTimeAdapter.TimeViewHolder>(TimeDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val binding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class TimeViewHolder(private val binding: ItemTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(showTime: ShowTimeDTO) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = inputFormat.parse(showTime.startTime!!)
            binding.tvTime.text = outputFormat.format(date!!)

            binding.root.setOnClickListener {
                    onItemClick(showTime)
                }
            }
    }

    class TimeDiffCallback : DiffUtil.ItemCallback<ShowTimeDTO>() {
        override fun areItemsTheSame(oldItem: ShowTimeDTO, newItem: ShowTimeDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: ShowTimeDTO, newItem: ShowTimeDTO): Boolean {
            return oldItem == newItem
        }
    }
}