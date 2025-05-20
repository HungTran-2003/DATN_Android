package haui.do_an.moive_ticket_booking.adapter.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import haui.do_an.moive_ticket_booking.DTO.TicketDTO
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.CustomTicketRvBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListTicketAdapter (
    private val context: Context,
    private val onItemClick: (TicketDTO) -> Unit
) : ListAdapter<TicketDTO, ListTicketAdapter.TicketViewHolder>(TicketDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = CustomTicketRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class TicketViewHolder(private val binding: CustomTicketRvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: TicketDTO) {
            val nameImage = ticket.posterUrl.split("\\").get(1)
            // Set movie poster with rounded corners using Glide
            Glide.with(context)
                .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transform(RoundedCorners(6))
                .into(binding.moviePoster)

            binding.movieTitle.text = ticket.movieName

            binding.movieDate.text = formatDate(ticket.startTime)

            val timeStart = formatTime(ticket.startTime)
            val timeEnd = formatTime(ticket.endTime)
            binding.movieTime.text = "$timeStart ~ $timeEnd"


            binding.theaterName.text = ticket.cinemaName
            binding.roomInfo.text = ticket.hallName
            binding.seatInfo.text = "${ticket.seat.row}${ticket.seat.number}"
            binding.ticketCode.text = ticket.ticketCode
            binding.ticketPrice.text = ticket.price.toString()

            binding.root.setOnClickListener {
                onItemClick(ticket)
            }
        }
    }

    private fun formatTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH-mm", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

    private fun formatDate(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

    class TicketDiffCallback : DiffUtil.ItemCallback<TicketDTO>() {
        override fun areItemsTheSame(oldItem: TicketDTO, newItem: TicketDTO): Boolean {
            return oldItem.ticketId == newItem.ticketId
        }

        override fun areContentsTheSame(oldItem: TicketDTO, newItem: TicketDTO): Boolean {
            return oldItem == newItem
        }
    }
}