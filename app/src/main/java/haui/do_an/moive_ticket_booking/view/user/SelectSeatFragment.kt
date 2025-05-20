package com.example.cinemaapp

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.gridlayout.widget.GridLayout
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.DTO.SeatDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentSelectSeatBinding
import haui.do_an.moive_ticket_booking.model.Booking
import haui.do_an.moive_ticket_booking.view.user.UserActivity
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SelectSeatFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectSeatBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var maxTicket = 9999
    private var ticket = 0


    private var isLoading: Boolean = true
    private var hallReady: Boolean = false
    private var seatReady: Boolean = false
    private lateinit var seats : List<SeatDTO>

    private val selectedSeats = ArrayList<Int>()
    private var ticketPrice: Double = 0.0
    private var totalPrice: Double = 0.0
    private var showtimeId: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        showtimeId = arguments?.getInt("showtimeId")!!
        val hallId = arguments?.getInt("hallId")
        ticketPrice = arguments?.getDouble("price")!!
        val userId = sharedPreferences.getInt("userId", 0)
        viewModel.getSeats(hallId!!)
        viewModel.getSeatReserved(showtimeId!!)
        viewModel.getTicketInfo(showtimeId, userId)
        val view = inflater.inflate(R.layout.fragment_select_seat, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSelectSeatBinding.bind(view)

        observeViewModel()
        clickConfirmButton()

    }

    private fun observeViewModel(){
        viewModel.seats.observe(viewLifecycleOwner) {
            isLoading = false
            hallReady = true
            seats = sortSeats(it)
            if (seatReady){
                updateUi()
            }
        }

        viewModel.seatIdReserved.observe(viewLifecycleOwner) {
            isLoading = false
            seatReady = true
            if (hallReady){
                updateUi()
            }
        }

        viewModel.ticketInfo.observe(viewLifecycleOwner) {
            it?.let {
                maxTicket = it.maxTicket
                ticket = it.ticketCount
            }
        }

        viewModel.Message.observe(viewLifecycleOwner) {
            it?.let {
                when(it){
                    "Đặt vé thành công" -> {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        (activity as UserActivity).backToHome()
                        viewModel.clearErrorMessage()
                    }
                    else -> {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.clearErrorMessage()
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun updateUi(){
        binding.viewSwitcher.displayedChild = 1
        seatSetup(seats)
    }

    private fun sortSeats(seats : List<SeatDTO>) : List<SeatDTO> {
        return seats.sortedWith(
            compareBy<SeatDTO> { it.rowNumbers }
                .thenBy { it.seatNumbers!!.toInt() }
        )
    }

    private fun seatSetup(seats: List<SeatDTO>){
        val reservedSeats = viewModel.seatIdReserved.value
        binding.seatContainer.removeAllViews()

        var rows = 0
        var cols = 0

        when (seats.size) {
            50 -> {
                rows = 5
                cols = 10
            }
            60 -> {
                rows = 6
                cols = 10
            }
            else -> {
                rows = 5
                cols = 8
            }
        }
        binding.seatContainer.rowCount = rows
        binding.seatContainer.columnCount = cols

        for (i in 0 until seats.size) {

            // Add seat number label
            val seatLabel = TextView(requireContext())
            val labelParams = GridLayout.LayoutParams()
            labelParams.width = dpToPx(20)
            labelParams.height = dpToPx(20)
            labelParams.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4))
            seatLabel.layoutParams = labelParams
            seatLabel.text = "${seats[i].rowNumbers}${seats[i].seatNumbers}"
            seatLabel.setTextColor(Color.WHITE)
            seatLabel.textSize = 10f
            seatLabel.gravity = Gravity.CENTER
            if (reservedSeats!!.contains(seats[i].id)) {
                seatLabel.setBackgroundResource(R.drawable.ic_seat_reserved)
            } else if(seats[i].typeSeat == "VIP") {
                seatLabel.setBackgroundResource(R.drawable.ic_seat_vip)
            } else {
                seatLabel.setBackgroundResource(R.drawable.ic_seat_available)
            }

            // Add seat to container
            binding.seatContainer.addView(seatLabel)

            // Set click listener (if not reserved)
            if (!reservedSeats.contains(seats[i].id)) {
                seatLabel.setOnClickListener {
                    if (selectedSeats.contains(seats[i].id)) {
                        selectedSeats.remove(seats[i].id)
                        if(seats[i].typeSeat == "VIP") {
                            seatLabel.setBackgroundResource(R.drawable.ic_seat_vip)
                            totalPrice -= ticketPrice * 1.5
                        } else {
                            seatLabel.setBackgroundResource(R.drawable.ic_seat_available)
                            totalPrice -= ticketPrice
                        }
                    } else {
                        if ((selectedSeats.size + ticket) < maxTicket) {
                            selectedSeats.add(seats[i].id!!)
                            seatLabel.setBackgroundResource(R.drawable.ic_seat_selected)
                            if (seats[i].typeSeat == "VIP") {
                                totalPrice += ticketPrice * 1.5
                            } else {
                                totalPrice += ticketPrice
                            }
                        } else {
                            Toast.makeText(requireContext(), "Bạn chỉ được đặt tối đa $maxTicket ghế", Toast.LENGTH_SHORT).show()
                        }
                    }
                    updateConfirmButton()
                }
            }
        }
    }

    private fun updateConfirmButton() {
        binding.btnConfirmSelection.text = "Xác nhận chọn ${selectedSeats.size} ghế - $totalPrice VND"
    }

    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }

    private fun clickConfirmButton(){
        val messageTextView = TextView(context).apply {
            text = messageBooking()
            textSize = 16f
            setPadding(32, 32, 32, 32)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
            }
        }
        binding.btnConfirmSelection.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận đặt vé")
                .setView(messageTextView)
                .setPositiveButton("Có") { dialog, _ ->
                    clickOk()
                    dialog.dismiss()
                }
                .setNegativeButton("Không") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToLogin()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun clickOk(){
        val bundle = Bundle()
        bundle.putInt("userId", sharedPreferences.getInt("userId", 0))
        bundle.putInt("showtimeId", showtimeId)
        bundle.putIntegerArrayList("selectedSeats", selectedSeats)
        bundle.putDouble("totalPrice", totalPrice)
        (activity as? UserActivity)?.navigateToPaymentComfirm(bundle)
    }

    private fun messageBooking(): String{
        val type = viewModel.movie.value!!.type
        val age = when (type) {
            "C13" -> "13 tuổi"
            "C16" -> "16 tuổi"
            "C18" ->   "18 tuổi"
            else -> "khác"
        }

        var header = "Tôi xác nhận mua vé cho người xem $age trở lên và đồng ý cung cấp giấy tờ tuỳ thân " +
                "để xác thực độ tuổi người xem."
        if (age == "khác"){
            header = " "
        }
        return header +
                "CineX sẽ không phục vụ khách hàng dưởi 16 tuổi cho các suất chiếu " +
                "từ 23:00. CineX sẽ không hoàn tiền nếu người xem không đáp ứng đủ điều kiện "
    }


}
