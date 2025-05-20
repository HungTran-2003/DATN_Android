package haui.do_an.moive_ticket_booking.adapter.admin

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.ItemShowtimeBinding

class ListShowTimeAdapter(
    private val context: Context,
    private val onItemClick: (ShowTimeDTO) -> Unit
) : ListAdapter<ShowTimeDTO, ListShowTimeAdapter.ShowTimeViewHolder>(ShowTimeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowTimeViewHolder {
        val binding = ItemShowtimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShowTimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowTimeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class ShowTimeViewHolder(private val binding: ItemShowtimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(showTime: ShowTimeDTO) {
            val nameImage = showTime.posterUrl?.split("\\")?.get(1)
            // Set movie poster with rounded corners using Glide
            Glide.with(context)
                .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transform(RoundedCorners(6))
                .into(binding.ivMoviePoster)

            binding.tvMovieTitle.text = showTime.movieName
            binding.tvStartTime.text = showTime.startTime?.replace("T", " ")
            binding.tvTicketsSold.text = showTime.numberSoldTickets.toString()
            binding.tvStatus.text = when (showTime.status) {
                "NOW_SHOWING" -> "Đang chiếu"
                "COMING_SOON" -> "Sắp chiếu"
                else -> "Đã chiếu"
            }
            val statusBackground = binding.tvStatus.background as GradientDrawable

            // Set different colors based on movie status
            val statusColor = when (showTime.status) {
                "NOW_SHOWING" -> R.color.status_now_showing
                "COMING_SOON" -> R.color.status_coming_soon
                else -> R.color.status_already_shown
            }
            statusBackground.setColor(ContextCompat.getColor(context, statusColor))

            binding.root.setOnClickListener {
                onItemClick(showTime)
            }
        }
    }

    class ShowTimeDiffCallback : DiffUtil.ItemCallback<ShowTimeDTO>() {
        override fun areItemsTheSame(oldItem: ShowTimeDTO, newItem: ShowTimeDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: ShowTimeDTO, newItem: ShowTimeDTO): Boolean {
            return oldItem == newItem
        }
    }
}