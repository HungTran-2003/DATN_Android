package haui.do_an.moive_ticket_booking.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentAddMovieBinding
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel

class AddMovieFragment : Fragment() {

    private val viewModel: AdminViewModel by activityViewModels()
    private var _binding: FragmentAddMovieBinding? = null
    private val binding get() = _binding!!
    private var isLoading = false

    private var movieDetail: MovieTmdbDTO? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_movie, container, false)
        _binding = FragmentAddMovieBinding.bind(view)

        // Hiển thị loading nếu cần
        val selectedMovie = arguments?.getBoolean("selectedMovie", false) ?: false
        isLoading = selectedMovie
        binding.viewSwitcher.displayedChild = if (isLoading) 0 else 1

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.movieDetail.observe(viewLifecycleOwner) { movieDetail ->
            movieDetail?.let {
                changeUIToMain(movieDetail)
                this.movieDetail = movieDetail
            }
            clickAddButton()
            setPopupMenu()
            clickButtonCancel()
        }

        viewModel.Message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (message.equals("Thêm phim thành công")) {
                    (activity as? AdminActivity)?.backToHomeFirm()
                } else{
                    movieDetail?.let {
                        changeUIToMain(movieDetail!!)
                    }
                }
            }
        }
    }

    private fun updateUI(movieDetail: MovieTmdbDTO){
        this.movieDetail = movieDetail
        binding.movieName.setText(movieDetail.title)
        binding.movieName.isEnabled = false
        binding.releaseYear.setText(movieDetail.release_date)
        binding.releaseYear.isEnabled = false
        binding.duration.setText(movieDetail.duration.toString())
        binding.duration.isEnabled = false
        binding.language.setText(movieDetail.language)
        binding.language.isEnabled = false
        binding.TMDBScore.setText(movieDetail.tmdbScore.toString())
        binding.TMDBScore.isEnabled = false
        binding.director.setText(movieDetail.director?.joinToString(", ") { it.name })
        binding.director.isEnabled = false
        binding.actors.setText(movieDetail.actors?.joinToString(", ") { it.name })
        binding.actors.isEnabled = false
        binding.genre.setText(movieDetail.genres?.joinToString(", ") { it.name })
        binding.genre.isEnabled = false
        binding.overview.setText(movieDetail.overview)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + movieDetail.backdrop_path)
            .placeholder(android.R.drawable.ic_menu_gallery) // Ảnh placeholder khi đang tải
            .error(android.R.drawable.ic_menu_report_image) // Ảnh khi tải lỗi
            .into(binding.imageBackdrop)

        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w500" + movieDetail.poster_path)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .into(binding.imageViewPoster)
    }

    private fun clickAddButton(){
        binding.buttonAddMovie.setOnClickListener {
            movieDetail?.let {
                movieDetail!!.overview = binding.overview.text.toString()
                movieDetail!!.type = binding.type.text.split("-").get(0)
                viewModel.addMovie(it)
                changeUIToLoading()
            }
        }
    }

    private fun changeUIToMain(movieDetail: MovieTmdbDTO) {
        isLoading = false
        binding.viewSwitcher.displayedChild = 1
        updateUI(movieDetail)
    }

    private fun changeUIToLoading() {
        isLoading = true
        binding.viewSwitcher.displayedChild = 0
    }

    private fun setPopupMenu(){
        binding.type.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.type)
            popupMenu.menuInflater.inflate(R.menu.type_movie, popupMenu.menu)

            // Bắt sự kiện chọn mục
            popupMenu.setOnMenuItemClickListener { menuItem ->
                binding.type.text = menuItem.title
                true
            }

            popupMenu.show()
        }
    }

    private fun clickButtonCancel(){
        binding.btCancel.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Bạn có chắc muốn huỷ thao tác này không?")
            builder.setPositiveButton("Có") { _, _ ->
                (activity as? AdminActivity)?.backToHomeFirm()
            }
            builder.setNegativeButton("Không") { dialog, _ ->
                dialog.dismiss()
            }
        }
    }


}