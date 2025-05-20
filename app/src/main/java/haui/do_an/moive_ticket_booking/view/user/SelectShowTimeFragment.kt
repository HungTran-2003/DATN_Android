package haui.do_an.moive_ticket_booking.view.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.user.ListDateAdapter
import haui.do_an.moive_ticket_booking.adapter.user.ListShowTimeAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentSelectShowTimeBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class SelectShowTimeFragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentSelectShowTimeBinding

    private var movieId: Int = 0
    private lateinit var adapterDate : ListDateAdapter
    private lateinit var adapterTime : ListShowTimeAdapter
    private lateinit var listShowTime : List<ShowTimeDTO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_select_show_time, container, false)
        binding = FragmentSelectShowTimeBinding.bind(view)

        movieId = arguments?.getInt("movieId") ?: 0
        binding.toolbarTitle.text = arguments?.getString("movieName") ?: ""
        viewModel.getShowTimes(movieId)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        adapterSetup()

        clickButtonBack()
    }

    private fun observeViewModel(){
        viewModel.showTimes.observe(viewLifecycleOwner) {
            val listDate = it?.map { it.startTime!!.split("T").get(0) }?.distinct()
            adapterDate.submitList(listDate)
            val listCinema = it?.filter { it.startTime!!.split("T").get(0) == listDate?.get(0)}
                ?.map { it.cinemaName }
                ?.distinct()

            adapterTime.submitList(listCinema)

        }
    }

    private fun adapterSetup(){
        adapterTime = ListShowTimeAdapter(requireContext(), emptyList())
        binding.rvShowTime.adapter = adapterTime
        adapterDate = ListDateAdapter(){ date ->
            val listShowTime = viewModel.showTimes.value?.filter { it.startTime!!.split("T").get(0) == date}
            val listCinema = listShowTime
                ?.map { it.cinemaName }
                ?.distinct()
            adapterTime.submitList(listCinema)
            adapterTime.updateList(listShowTime!!)
            val dateInfo = viewModel.showTimes.value?.filter { it.startTime!!.split("T").get(0) == date}
                ?.get(0)
                ?.startTime.toString()

            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEEE, dd 'tháng' MM, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateInfo)
            binding.dateInfo.text = outputFormat.format(date!!)
        }
        binding.rvDate.adapter = adapterDate
    }

    private fun clickButtonBack(){
        binding.backButton.setOnClickListener {
            (activity as UserActivity).backToFilmDetail()
        }
    }
}