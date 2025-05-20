package haui.do_an.moive_ticket_booking.view.admin

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentAddShowtimeBinding
import java.util.Calendar
import android.app.TimePickerDialog
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import haui.do_an.moive_ticket_booking.DTO.CinemaDTO
import haui.do_an.moive_ticket_booking.api.ApiRoutes
import haui.do_an.moive_ticket_booking.DTO.CityDTO
import haui.do_an.moive_ticket_booking.DTO.HallDTO
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.model.ShowTime
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Objects
import kotlin.getValue

class AddShowtimeFragment : Fragment() {
    private val viewModel : AdminViewModel by activityViewModels()

    private lateinit var binding: FragmentAddShowtimeBinding
    private val calendar = Calendar.getInstance()

    private lateinit var showtime: ShowTimeDTO
    private var selectShowtime: Boolean = false
    private var selectTime: Boolean = false
    private var selectDate: Boolean = false
    private var movieId: Int = -1
    private var cinemaId: Int = -1
    private var hallId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_add_showtime, container, false)
        binding = FragmentAddShowtimeBinding.bind(view)
        val showtimeId = arguments?.getInt("showtimeId", 0)
        var nameImage = arguments?.getString("posterUrl")?.split("\\")?.get(1)
        if (showtimeId != 0){
            showtime = viewModel.showTimes.value!!.find { it.id == showtimeId }!!
            movieId = showtime.movieId!!
            selectShowtime = true
            updateUI(showtime!!)
            nameImage = showtime.posterUrl?.split("\\")?.get(1)

        } else {
            binding.tvMovieTitle.text = arguments?.getString("title")
            movieId = arguments?.getInt("id")!!
            val duration = arguments?.getInt("duration")
            if (duration != null) {
                binding.tvMovieDuration.text = "Thời lượng: $duration phút"
            }
        }
        // Set movie poster with rounded corners using Glide
        Glide.with(requireContext())
            .load(ApiRoutes.BASE_URL + ApiRoutes.Movie.IMAGE + nameImage)
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .transform(RoundedCorners(6))
            .into(binding.ivMoviePoster)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
        binding.btnSelectTime.setOnClickListener {
            showTimePickerDialog()
        }

        setAdapterSpinnerCinema()
        clickAddButton()
        message()

        binding.btBack.setOnClickListener {
            clickButtonBack()
        }

        if (selectShowtime){
            updateSpinner(showtime)
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateView()
                if (selectTime == true && selectDate == true){
                    calculateEndTime()
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            R.style.TimePickerTheme,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeView()
                if (selectTime == true && selectDate == true){
                    calculateEndTime()
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true // 24-hour format
        )
        timePickerDialog.show()
    }

    private fun updateDateView() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        selectDate = true
        binding.tvSelectedDate.text = "Ngày: $formattedDate"
    }

    private fun updateTimeView() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat.format(calendar.time)
        binding.tvSelectedTime.visibility = View.VISIBLE
        selectTime = true
        binding.tvSelectedTime.text = "Thời gian: $formattedTime"
    }

    private fun calculateEndTime() {
        val currentTime = Calendar.getInstance()
        if (!calendar.after(currentTime)){
            binding.tvSelectedDate.text = "Vui lòng chọn thời gian trong tương lai!"
            binding.tvSelectedTime.visibility = View.GONE
            selectTime = false
            selectDate = false
        } else{
            val endCalendar = calendar.clone() as Calendar
            endCalendar.timeInMillis = calendar.timeInMillis
            endCalendar.add(Calendar.MINUTE, arguments?.getInt("duration")!!)

            val timeFormat = SimpleDateFormat("dd-MM-yyyy-HH:mm", Locale.getDefault())
            val formattedEndTime = timeFormat.format(endCalendar.time)
            binding.tvCalculatedEndTime.text = "$formattedEndTime"
        }
    }

    private fun setAdapterSpinnerCinema(){
        viewModel.getCinemas()
        viewModel.cinemas.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.spinnerBranch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selectedCinema : CinemaDTO = it[position]
                        cinemaId = selectedCinema.id
                        setAdapterSpinnerRoom(selectedCinema.id)
                        Log.d("CinemaId", cinemaId.toString())
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
                binding.spinnerBranch.adapter = adapter
            }
        }
    }

    private fun setAdapterSpinnerRoom(cinemaId: Int){
        viewModel.getHall(cinemaId)
        viewModel.hall.observe(viewLifecycleOwner) { hall ->
            if (hall != null) {
                binding.spinnerRoom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        val selectedHall : HallDTO = hall[position]
                        hallId = selectedHall.id
                        Log.d("HallId", hallId.toString())
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hall)
                binding.spinnerRoom.adapter = adapter
            }
        }
    }

    private fun clickAddButton(){
        binding.btnAddShowtime.setOnClickListener {
            val error = validateValue()
            if (error == ""){
                val timeFormat = SimpleDateFormat("dd-MM-yyyy-HH:mm", Locale.getDefault())
                val startDate = timeFormat.format(calendar.time)
                val endDate = binding.tvCalculatedEndTime.text.toString()
                val ticketPrice = binding.etTicketPrice.text.toString()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = dateFormat.format(calendar.time)
                val showtime = ShowTime(
                    id = null,
                    movieId = movieId,
                    cinemaId = cinemaId,
                    hallId = hallId,
                    startTime = startDate,
                    endTime =endDate,
                    price = ticketPrice.toDouble(),
                    date = date,
                    maxTicket = binding.etTicketNumber.text.toString().toInt()
                )
                if (selectShowtime){
                    viewModel.updateShowTime(this.showtime.id, showtime)
                } else{
                    viewModel.addShowTime(showtime)
                }
                changeToLoadingUi()
            } else{
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateValue(): String{
        if (movieId == -1) return "Lỗi khi lấy Id phim"
        if (!selectTime) return "Chưa chọn thời gian bắt đầu"
        if (!selectDate) return "Chưa chọn ngày chiếu"
        if (binding.etTicketPrice.text.isNullOrEmpty()) return "Chưa nhập giá vé"
        if (hallId == -1) return "Chưa chọn phòng chiếu"
        if (cinemaId == -1) return "Chưa chọn rạp chiếu"
        return ""
    }

    private fun message(){
        viewModel.Message.observe(viewLifecycleOwner) { message ->
            message?.let {
                if (message.equals("Thêm thành công")){
                    Toast.makeText(requireContext(), "Thêm suất chiếu cho phim ${binding.tvMovieTitle.text} thành công", Toast.LENGTH_SHORT).show()
                    (activity as AdminActivity).backToHomeShowtime()
                }
                else if (message.equals("Thêm thất bại")){
                    Toast.makeText(requireContext(), "Thêm suất chiếu cho phim ${binding.tvMovieTitle.text} thất bại", Toast.LENGTH_SHORT).show()
                    changeToMainUi()
                }  else if (message.equals("Cập nhật thành công")){
                    Toast.makeText(requireContext(), "Cập nhật suất chiếu cho phim ${binding.tvMovieTitle.text} thành công", Toast.LENGTH_SHORT).show()
                    (activity as AdminActivity).backToHomeShowtime()
                } else {
                    Toast.makeText(requireContext(), "Lỗi: $message", Toast.LENGTH_SHORT).show()
                    changeToMainUi()
                }
            }
        }
    }

    private fun changeToLoadingUi(){
        binding.viewSwitcher.displayedChild = 1
    }

    private fun changeToMainUi(){
        binding.viewSwitcher.displayedChild = 0
    }

    private fun clickButtonBack(){
        (activity as AdminActivity).backToHomeShowtime()
    }

    private fun updateUI(showtime: ShowTimeDTO){
        binding.tvMovieTitle.text = showtime.movieName
        binding.tvMovieDuration.text = showtime.duration.toString()

        binding.tvSelectedTime.visibility = View.VISIBLE

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(showtime.startTime)
        calendar.time = date

        val dateFormat1 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat1.format(calendar.time)
        selectDate = true
        binding.tvSelectedDate.text = "Ngày: $formattedDate"

        val timeFormat1 = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedTime = timeFormat1.format(calendar.time)
        selectTime = true
        binding.tvSelectedTime.text = "Thời gian: $formattedTime"

        val endCalendar = calendar.clone() as Calendar
        endCalendar.timeInMillis = calendar.timeInMillis
        endCalendar.add(Calendar.MINUTE, showtime.duration!!)

        val timeFormat = SimpleDateFormat("dd-MM-yyyy-HH:mm", Locale.getDefault())
        val formattedEndTime = timeFormat.format(endCalendar.time)
        binding.tvCalculatedEndTime.text = "$formattedEndTime"

        binding.etTicketPrice.setText(showtime.ticketPrice.toString())
        binding.etTicketNumber.setText(showtime.numberSoldTickets.toString())

        binding.btnAddShowtime.text = "Cập nhật suất chiếu"
    }

    private fun updateSpinner(showtime: ShowTimeDTO){
//        outer@ for (i in 0 until binding.spinnerBranch.count) {
//            binding.spinnerBranch.setSelection(i)
//            for (j in 0 until binding.spinnerRoom.count) {
//                val selectedCinema : CinemaDTO = viewModel.cinemas.value!![i]
//                if (selectedCinema.id == showtime.hallId){
//                    binding.spinnerRoom.setSelection(j)
//                    break@outer
//                }
//            }
//        }
    }
}