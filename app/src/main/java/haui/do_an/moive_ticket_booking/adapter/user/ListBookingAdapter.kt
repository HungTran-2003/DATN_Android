package haui.do_an.moive_ticket_booking.adapter.user

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
import haui.do_an.moive_ticket_booking.DTO.BookingDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.CustomItemBookingListBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListBookingAdapter(
    private val context: Context,
    private val onItemClick: (BookingDTO) -> Unit
) : ListAdapter<BookingDTO, ListBookingAdapter.BoockingVewHolder>(BoockingDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoockingVewHolder {
        val binding = CustomItemBookingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoockingVewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoockingVewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class BoockingVewHolder(private val binding: CustomItemBookingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: BookingDTO) {
            val nameImage = booking.posterUrl.split("\\").get(1)
            Glide.with(context)
                .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transform(RoundedCorners(6))
                .into(binding.moviePoster)

            binding.movieTitle.text = booking.movieName

            binding.movieDate.text = formatDate(booking.startTime)

            val timeStart = formatTime(booking.startTime)
            val timeEnd = formatTime(booking.endTime)
            binding.movieTime.text = "$timeStart ~ $timeEnd"

            binding.tvBookingStatus.text = if (booking.status == "PENDING") "Chưa thanh toán" else "Đã thanh toán"

            val statusBackground = binding.tvBookingStatus.background as GradientDrawable

            val statusColor = when (booking.status) {
                "PENDING" -> R.color.status_undetermined
                else -> R.color.status_coming_soon
            }
            statusBackground.setColor(ContextCompat.getColor(context, statusColor))

            binding.theaterName.text = booking.cinemaName
            binding.roomInfo.text = booking.hallName
            binding.seatInfo.text = booking.seats.joinToString(", ") { it.seatName }
            binding.ticketPrice.text = booking.totalPrice.toString()

            binding.root.setOnClickListener {
                onItemClick(booking)
            }
        }
    }

    private fun formatTime(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH-mm", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

    private fun formatDate(input: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(input)
        return outputFormat.format(date!!)
    }

    class BoockingDiffCallback : DiffUtil.ItemCallback<BookingDTO>() {
        override fun areItemsTheSame(oldItem: BookingDTO, newItem: BookingDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: BookingDTO, newItem: BookingDTO): Boolean {
            return oldItem == newItem
        }
    }
}