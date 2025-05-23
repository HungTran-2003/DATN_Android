package haui.do_an.moive_ticket_booking.view.user

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.databinding.FragmentPaymentConfirmBinding
import haui.do_an.moive_ticket_booking.model.Booking
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.contains
import kotlin.math.log

@AndroidEntryPoint
class PaymentConfirmFragment : Fragment() {

    private lateinit var binding: FragmentPaymentConfirmBinding

    private val viewModel : UserViewModel by activityViewModels()

    private var userId : Int = 0
    private var showTimeId : Int = 0
    private var totalPrice : Double = 0.0
    private var selectedSeats = mutableListOf<Int>()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment_confirm, container, false)
        binding = FragmentPaymentConfirmBinding.bind(view)

        userId = arguments?.getInt("userId")?: 0
        showTimeId = arguments?.getInt("showtimeId")!!
        totalPrice = arguments?.getDouble("totalPrice")!!.toDouble()
        selectedSeats = arguments?.getIntegerArrayList("selectedSeats")!!

        updateUi(selectedSeats, totalPrice)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        clickBtnChoseCoupon()
        clickButtonBack()

        clickButtonPayment()
    }

    private fun observeViewModel(){
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                showErrorDialog(it)
            }
        }

        viewModel.Message.observe(viewLifecycleOwner) {
            it?.let {
                if (it == "Đặt vé thành công"){
                    (activity as UserActivity).backToHome()
                    viewModel.clearErrorMessage()
                }
            }
        }
    }

    private fun updateUi(
        selectSeat: List<Int>,
        totalPrice: Double){

        val nameImage = viewModel.movie.value!!.posterUrl.split("\\").get(1)
        Glide.with(requireContext())
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(binding.moviePoster)

        binding.movieTitle.text = viewModel.movie.value!!.title
        binding.movieType.text = when(viewModel.movie.value!!.type){
            "C13" -> "C13-Phim được phổ biến đến người xem từ đủ 13 tuổi trở lên."
            "C16" -> "C16-Phim được phổ biến đến người xem từ đủ 16 tuổi trở lên."
            "C18" -> "C18-Phim được phổ biến đến người xem từ đủ 18 tuổi trở lên."
            else -> "Phim được phổ biến đến người xem"
        }

        Log.d("showtime", viewModel.showTimes.value.toString())
        Log.d("showtimeId", showTimeId.toString())

        val showtime = viewModel.showTimes.value!!.find { it.id == showTimeId }!!
        val startT = formatTime(showtime.startTime!!)
        val endT = formatTime(showtime.endTime!!)
        val date = formatDate(showtime.startTime.split("T")[0])

        binding.movieDate.text = date
        binding.movieTime.text = "$startT ~ $endT"
        binding.movieTheater.text = showtime.cinemaName
        binding.movieScreen.text = showtime.hallName
        val seats = viewModel.seats.value!!.filter { it.id in selectSeat }
            .joinToString(", ") { it.rowNumbers + it.seatNumbers }
        binding.seatInfo.text = "Ghế: $seats"
        binding.totalPrice.text = "Tổng giá vé: $totalPrice"

        binding.quantityValue.text = selectSeat.size.toString()
        binding.subtotalValue.text = totalPrice.toString()
        binding.ticketPriceValue.text = totalPrice.toString()

        binding.totalValue.text = totalPrice.toString()

    }

    private fun formatDate(date: String) : String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
        val date = inputFormat.parse(date)
        return outputFormat.format(date!!)
    }

    private fun formatTime(time: String) : String{
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(time)
        return outputFormat.format(date!!)
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")

        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
            Log.d("error", error)
        }
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()

    }

    private fun clickBtnChoseCoupon(){
        binding.choseCouponButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("userId", userId)
            bundle.putInt("hallId", viewModel.showTimes.value!!.find { it.id == showTimeId }!!.hallId)
            (activity as UserActivity).navigateToChoseCoupon(bundle)
        }
    }

    override fun onResume() {
        super.onResume()
        val code : String? = viewModel.CouponCode
        code?.let {
            binding.couponInput.setText(code)
            updateTotalPayment(viewModel.discound!!)
        }
    }

    private fun updateTotalPayment(discount: Double){
        val priceDiscount = ((totalPrice * discount) / 100).toInt()
        binding.discountValue.text = priceDiscount.toString()
        binding.totalValue.text = (totalPrice - priceDiscount).toString()
    }

    private fun clickButtonBack(){
        binding.backButton.setOnClickListener {
            (activity as UserActivity).backToSelectSeat()
        }

    }

    private fun clickButtonPayment(){
        binding.confirmButton.setOnClickListener {
            val booking = Booking(
                id = 0,
                userId = userId,
                showtimeId = showTimeId,
                totalPrice = totalPrice.toInt(),
                seatIds = selectedSeats,
                couponCode = viewModel.CouponCode
            )
            viewModel.createBooking(booking)
//            val request = mapOf(
//                "productName" to "sản phâm 1",
//                "description" to "vé",
//                "returnUrl" to "myapp://success",
//                "cancelUrl" to "myapp://cancel",
//                "price" to 5000
//            )
//            viewModel.createLinkPayment(request)
            (activity as UserActivity).navigateToPayment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.CouponCode = null
        viewModel.discound = null
    }
}