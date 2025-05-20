package haui.do_an.moive_ticket_booking.adapter.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.CustomAutocompleteItemBinding
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import android.widget.Filter

class SearchMovieAdapter(
    private val context: Context
) : ArrayAdapter<MovieDTO>(context, R.layout.custom_autocomplete_item, mutableListOf()), Filterable {

    private var items: List<MovieDTO> = emptyList()
    private var filteredItems: List<MovieDTO> = emptyList()

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieDTO>() {
        override fun areItemsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieDTO, newItem: MovieDTO): Boolean {
            return oldItem == newItem
        }
    }

    private val listAdapter = object : ListAdapter<MovieDTO, RecyclerView.ViewHolder>(DIFF_CALLBACK) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            throw UnsupportedOperationException()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            throw UnsupportedOperationException()
        }
    }

    fun submitList(newList: List<MovieDTO>) {
        // Sử dụng ListAdapter để tính toán sự khác biệt
        listAdapter.submitList(newList) {
            // Sau khi tính toán hoàn tất, cập nhật danh sách
            items = newList
            filteredItems = newList
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = filteredItems.size

    override fun getItem(position: Int): MovieDTO = filteredItems[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.custom_autocomplete_item, parent, false)

        val binding = CustomAutocompleteItemBinding.bind(view)
        binding.movieName.text = getItem(position).title
        val nameImage = getItem(position).posterUrl.split("\\").get(1)
        Glide.with(context)
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
            .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh placeholder khi đang tải
            .error(android.R.drawable.ic_menu_report_image) // Ảnh khi tải lỗi
            .into(binding.posterMovie)
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val query = constraint?.toString()?.lowercase() ?: ""

                filteredItems = if (query.isEmpty()) {
                    items
                } else {
                    items.filter { it.title.lowercase().contains(query) }
                }

                filterResults.values = filteredItems
                filterResults.count = filteredItems.size
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                filteredItems = results?.values as? List<MovieDTO> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }
}