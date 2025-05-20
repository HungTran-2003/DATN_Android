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
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.CustomItemMovieListBinding
import haui.do_an.moive_ticket_booking.databinding.ItemMovieBinding
import java.util.Locale

class ListMovieAdapter(
    private val context: Context,
    private val onItemClick: (MovieDTO) -> Unit
) : ListAdapter<MovieDTO, ListMovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDTO) {
            val nameImage = movie.posterUrl.split("\\").get(1)
            // Set movie poster with rounded corners using Glide
            Glide.with(context)
                .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transform(RoundedCorners(6))
                .into(binding.ivMoviePoster)

            binding.tvMovieTitle.text = movie.title
            binding.tvMovieDuration.text = "${movie.duration} phút"
            binding.rbMovieRating.rating = movie.averageRating!!
            binding.tvReviewCount.text = movie.totalRatings.toString()

            binding.root.setOnClickListener {
                onItemClick(movie)
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<MovieDTO>() {
        override fun areItemsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem == newItem
        }
    }
}