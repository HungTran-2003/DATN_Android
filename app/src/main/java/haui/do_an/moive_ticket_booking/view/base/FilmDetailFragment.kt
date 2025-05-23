package haui.do_an.moive_ticket_booking.view.base

import android.content.SharedPreferences
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.FragmentFilmDetailBinding
import haui.do_an.moive_ticket_booking.view.admin.AdminActivity
import haui.do_an.moive_ticket_booking.view.user.UserActivity
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FilmDetailFragment : Fragment() {

    private lateinit var binding: FragmentFilmDetailBinding

    private val viewModelUser: UserViewModel by activityViewModels()
    private val viewModelAdmin: AdminViewModel by activityViewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var movieId: Int = 0
    private lateinit var role : String
    private var uriPoster : Uri? = null
    private var uriBanner : Uri? = null
    private lateinit var selectedItem: MovieDTO
    private var likeMovie = false
    private var userId = 0

    private var targetImageView: ImageView? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            targetImageView?.let { imageView ->
                updateImage(it, imageView)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_film_detail, container, false)
        movieId = arguments?.getInt("movieId")?: 0
        role = sharedPreferences.getString("role", "").toString()
        userId = sharedPreferences.getInt("userId", 0)
        binding = FragmentFilmDetailBinding.bind(view)
        when(role){
            "USER" -> {
                binding.tvMovieStatus.visibility = View.GONE
                viewModelUser.getMovieDetail(movieId, userId)
                observeViewModelUser(viewModelUser)
            }
            "ADMIN" -> {
                viewModelAdmin.getMovieDetail(movieId, userId)
                observeViewModelAdmin(viewModelAdmin)
                updateUiAdmin()
            }
            else -> throw IllegalArgumentException("Invalid role: $role")
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        clickButtonBookTicket()
        clickButtonBack()

        clickMoviePoster()
        clickMovieBanner()
        if (role.equals("ADMIN")){
            clickAddShowTime()
        }

        clickImLikeMovie()
    }

    private fun observeViewModelAdmin(viewModel: AdminViewModel) {
        viewModel.movie.observe(viewLifecycleOwner) {
            it?.let {
                binding.contentSwitcher.displayedChild = 1
                updateUi(it)
                selectedItem = it
            }
        }
        viewModel.Message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (message.equals("Thêm phim thành công")) {
                    (activity as? AdminActivity)?.backToHomeFirm()
                }
                binding.contentSwitcher.displayedChild = 1
            }
        }
    }

    private fun observeViewModelUser(viewModel: UserViewModel) {

        viewModel.movie.observe(viewLifecycleOwner) {
            it?.let {
                binding.contentSwitcher.displayedChild = 1
                updateUi(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
                viewModel.clearErrorMessage()
            }
        }
    }

    private fun updateUi(movie: MovieDTO){

        val bannerImage = movie.backdropPath?.split("\\")?.get(1)
        Glide.with(requireContext())
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + bannerImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(binding.movieBanner)

        val nameImage = movie.posterUrl.split("\\").get(1)
        Glide.with(requireContext())
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(binding.moviePoster)

        binding.movieTitle.text = movie.title
        binding.releaseDate.text = movie.releaseDate
        binding.duration.text = movie.duration.toString()
        binding.likesCount.text = movie.totalFavorites.toString()
        binding.movieDescription.text = movie.description

        setUpBackgroundStatus(movie.status!!)
        if (movie.status == "COMING_SOON"){
            binding.bookTicketButton.visibility = View.GONE
        } else {
            binding.bookTicketButton.visibility = View.VISIBLE
        }
        binding.tvMovieStatus.text = when (movie.status) {
            "UNDETERMINED" -> "Chưa xác định"
            "NOW_SHOWING" -> "Đang chiếu"
            "COMING_SOON" -> "Sắp chiếu"
            "SPECIAL" -> "Đặc biệt"
            else -> "Đã chiếu"
        }

        when(movie.type){
            "C13" -> binding.tvMovieType.text = "C13-Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên."
            "C16" -> binding.tvMovieType.text = "C16-Phim được phổ biến đến người xem từ đủ 16 tuổi trở lên."
            "C18" -> binding.tvMovieType.text = "C18-Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên."
            else -> binding.tvMovieType.text = "P-Phim phù hợp cho mọi lứa tuổi"
        }

        binding.tvGenres.text = movie.genres?.joinToString(", ") {it.name}
        binding.tvActors.text = movie.actors?.joinToString(", ") {it.name}
        binding.tvDirectors.text = movie.directors?.joinToString(", ") {it.name}

        if (movie.language.equals("Tiếng Việt")){
            binding.tvLanguage.text = "Tiếng Việt"
        } else{
            binding.tvLanguage.text = "${movie.language} - Phụ đề Tiếng Việt"
        }

        if(movie.favortive!!){
            likeMovie = movie.favortive!!
            binding.imLikeMovie.setImageResource(R.drawable.ic_heart_full)
        } else{
            binding.imLikeMovie.setImageResource(R.drawable.ic_heart)
        }
    }

    private fun updateUiAdmin(){
        binding.moviePoster.isClickable = true
        binding.movieBanner.isClickable = true
        binding.tvMovieType.isClickable = true
        binding.bookTicketButton.text = "Cập nhật"
        binding.btnAddShowtime.visibility = View.VISIBLE
        binding.imLikeMovie.isClickable = false
        setupPopupMenu()
    }

    private fun setUpBackgroundStatus(status: String){
        val background = binding.tvMovieStatus.background as GradientDrawable
        val statusColor = when (status) {
            "UNDETERMINED" -> R.color.status_undetermined
            "NOW_SHOWING" -> R.color.status_now_showing
            "COMING_SOON" -> R.color.status_coming_soon
            "SPECIAL" -> R.color.status_special
            else -> R.color.status_already_shown
        }
        background.setColor(ContextCompat.getColor(requireContext(), statusColor))

    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")
        Log.d("error", error)

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToHome()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun clickButtonBookTicket(){
        binding.bookTicketButton.setOnClickListener {
            if (role.equals("USER")) {
                val bundle = Bundle()
                bundle.putInt("movieId", movieId)
                bundle.putString("movieName", binding.movieTitle.text.toString())
                (activity as UserActivity).navigateToSelectShowTime(bundle)
            } else {
                val status = when (binding.tvMovieStatus.text) {
                    "Chưa xác định" -> "UNDETERMINED"
                    "Đặc biệt" -> "SPECIAL"
                    "Sắp chiếu" -> "COMING_SOON"
                    else -> "null"
                }
                viewModelAdmin.updateMovie(movieId,
                    binding.tvMovieType.text.toString().split("-").get(0).trim(),
                    status,
                    uriPoster, uriBanner, requireContext())

                binding.contentSwitcher.displayedChild = 0

            }
        }
    }

    private fun clickButtonBack(){
        binding.backButton.setOnClickListener {
            if (role.equals("USER")){
                (activity as UserActivity).backToHome()
            } else {
                (activity as AdminActivity).backToHomeFirm()
            }
        }
    }

    private fun clickMoviePoster(){
        binding.moviePoster.setOnClickListener {
            targetImageView = binding.moviePoster
            getContent.launch("image/*")
        }
    }

    private fun clickMovieBanner(){
        binding.movieBanner.setOnClickListener {
            targetImageView = binding.movieBanner
            getContent.launch("image/*")
        }
    }

    private fun clickAddShowTime(){
        binding.btnAddShowtime.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("id", selectedItem.id)
            bundle.putString("title", selectedItem.title)
            bundle.putString("posterUrl", selectedItem.posterUrl)
            bundle.putInt("duration", selectedItem.duration!!)
            (activity as AdminActivity).navigateToAddShowtime(bundle)
        }

    }

    private fun setupPopupMenu(){
        binding.tvMovieType.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.tvMovieType)
            popupMenu.menuInflater.inflate(R.menu.type_movie, popupMenu.menu)

            // Bắt sự kiện chọn mục
            popupMenu.setOnMenuItemClickListener { menuItem ->
                binding.tvMovieType.text = menuItem.title
                true
            }
            popupMenu.show()
        }

        binding.tvMovieStatus.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.tvMovieStatus)
            popupMenu.menuInflater.inflate(R.menu.status_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                binding.tvMovieStatus.text = menuItem.title
                val status = resources.getResourceEntryName(menuItem.itemId)
                Log.d("text", status)
                setUpBackgroundStatus(status)
                true
            }
            popupMenu.show()
        }
    }

    private fun updateImage(uri: Uri, imageView: ImageView){
        if (imageView == binding.moviePoster){
            uriPoster = uri
        } else {
            uriBanner = uri
        }
        Glide.with(requireContext())
            .load(uri)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(imageView)
    }

    private fun clickImLikeMovie(){
        binding.imLikeMovie.setOnClickListener {
            if (likeMovie){
                binding.imLikeMovie.setImageResource(R.drawable.ic_heart)
                binding.likesCount.text = (binding.likesCount.text.toString().toInt() - 1).toString()
                likeMovie = false
                viewModelUser.deleteMovieFavorite(userId, movieId)
            } else {
                binding.imLikeMovie.setImageResource(R.drawable.ic_heart_full)
                binding.likesCount.text = (binding.likesCount.text.toString().toInt() + 1).toString()
                viewModelUser.addMovieFavorite(userId, movieId)
                likeMovie = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}