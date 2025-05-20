package haui.do_an.moive_ticket_booking.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.FragmentReviewBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel


class ReviewFragment : Fragment() {

    private lateinit var binding: FragmentReviewBinding
    private val viewModel: UserViewModel by activityViewModels()

    private var bookingId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review, container, false)
        binding = FragmentReviewBinding.bind(view)
        bookingId = arguments?.getInt("bookingId") ?: 0
        updateUi()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickRatting()
    }

    private fun updateUi(){
        binding.tvMovieTitle.text = viewModel.bookings!!.value?.find { it.id == bookingId }?.movieName
        val nameImage = viewModel.bookings!!.value?.find { it.id == bookingId }?.posterUrl!!.split("\\").get(1)
        Glide.with(requireContext())
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(binding.ivMoviePoster)
    }

    private fun clickRatting(){
        binding.ratingBar.setOnRatingBarChangeListener({ rBar, rating, fromUser ->
            val ratingInt = rating.toInt()
            binding.tvRatingValue.setText(ratingInt.toString() + "/5")
        })

    }

}