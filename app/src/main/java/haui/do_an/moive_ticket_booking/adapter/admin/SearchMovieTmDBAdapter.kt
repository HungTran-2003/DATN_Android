package haui.do_an.moive_ticket_booking.adapter.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.DTO.SearchMovies

class SearchMovieTmDBAdapter(
    context: Context,
) : ArrayAdapter<MovieTmdbDTO>(context, 0, mutableListOf()) {

    private val items = mutableListOf<MovieTmdbDTO>()

    private final val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w500"

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.custom_autocomplete_item, parent, false)
        val title = view.findViewById<TextView>(R.id.movieName)
        val image = view.findViewById<ImageView>(R.id.posterMovie)
        val movie = getItem(position)
        if (movie != null) {
            title.text = movie.title
            Glide.with(context)
                .load(BASE_URL_IMAGE + movie.poster_path)
                .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh placeholder khi đang tải
                .error(android.R.drawable.ic_menu_report_image) // Ảnh khi tải lỗi
                .into(image)
        }
        return view
    }

    fun update(movies: SearchMovies) {
        items.clear()
        items.addAll(movies.results)

        clear()
        addAll(movies.results)
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply {
                    values = items
                    count = items.size
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                notifyDataSetChanged()
            }
        }
    }


}