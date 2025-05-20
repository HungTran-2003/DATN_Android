package haui.do_an.moive_ticket_booking.adapter.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.DTO.CinemaDTO
import haui.do_an.moive_ticket_booking.databinding.CustomItemMovieListBinding
import haui.do_an.moive_ticket_booking.databinding.CustomListCinemaItemBinding

class ListCinemaAdapter (
    private val onItemClick: (CinemaDTO) -> Unit
) : ListAdapter<CinemaDTO, ListCinemaAdapter.CinemaViewHolder>(CinemaDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val binding = CustomListCinemaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CinemaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class CinemaViewHolder(private val binding: CustomListCinemaItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cinema: CinemaDTO) {

            binding.tvCinemaName.text = cinema.name
            binding.tvCinemaAddress.text = cinema.address
            binding.tvCinemaPhone.text = cinema.contactInfo
            binding.root.setOnClickListener {
                onItemClick(cinema)
            }
        }
    }

    class CinemaDiffCallback : DiffUtil.ItemCallback<CinemaDTO>() {
        override fun areItemsTheSame(oldItem: CinemaDTO, newItem: CinemaDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: CinemaDTO, newItem: CinemaDTO): Boolean {
            return oldItem == newItem
        }
    }
}