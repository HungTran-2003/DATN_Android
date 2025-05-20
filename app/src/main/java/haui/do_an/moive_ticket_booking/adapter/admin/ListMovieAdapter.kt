package haui.do_an.moive_ticket_booking.adapter.admin

import haui.do_an.moive_ticket_booking.R
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.CustomItemMovieListBinding
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import java.util.Locale

class ListMovieAdapter(
    private val context: Context,
    private val onItemClick: (MovieDTO) -> Unit,
    private val onLongItemClick: (MovieDTO) -> Unit
) : ListAdapter<MovieDTO, ListMovieAdapter.MovieViewHolder>(MovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = CustomItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class MovieViewHolder(private val binding: CustomItemMovieListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: MovieDTO) {
            val nameImage = movie.posterUrl.split("\\").get(1)
            // Set movie poster with rounded corners using Glide
            Glide.with(context)
                .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e("Glide", "Load failed: $e")
                        e?.logRootCauses("Glide") // In nguyên nhân gốc rễ
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable?>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d("Glide", "Load successful")
                        return false
                    }
                })
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_report_image)
                .transform(RoundedCorners(6))
                .into(binding.ivMoviePoster)

            // Set movie title
            binding.tvMovieTitle.text = movie.title

            // Set movie duration
            binding.tvMovieDuration.text = movie.duration.toString()

            // Set movie rating
            binding.rbMovieRating.rating = movie.averageRating!!
            binding.tvMovieRatingValue.text = String.format(Locale.getDefault(), "%.1f/5", movie.averageRating)

            // Set movie status with appropriate background color
            val statusBackground = binding.tvMovieStatus.background as GradientDrawable

            // Set different colors based on movie status
            val statusColor = when (movie.status) {
                "UNDETERMINED" -> R.color.status_undetermined
                "NOW_SHOWING" -> R.color.status_now_showing
                "COMING_SOON" -> R.color.status_coming_soon
                "SPECIAL" -> R.color.status_special
                else -> R.color.status_already_shown
            }
            statusBackground.setColor(ContextCompat.getColor(context, statusColor))
            binding.tvMovieStatus.text = when (movie.status) {
                "UNDETERMINED" -> "Chưa xác định"
                "NOW_SHOWING" -> "Đang chiếu"
                "COMING_SOON" -> "Sắp chiếu"
                "SPECIAL" -> "Đặc biệt"
                else -> "Đã chiếu"
            }

            binding.root.setOnClickListener {
                onItemClick(movie)
            }

            binding.root.setOnLongClickListener {
                onLongItemClick(movie)
                true
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