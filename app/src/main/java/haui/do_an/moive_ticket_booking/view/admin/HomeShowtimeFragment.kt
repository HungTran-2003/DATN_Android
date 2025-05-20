package haui.do_an.moive_ticket_booking.view.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.admin.SearchMovieAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeShowtimeBinding
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.adapter.admin.ListShowTimeAdapter
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel
import kotlinx.coroutines.Job
import kotlin.getValue

class HomeShowtimeFragment : Fragment() {

    private lateinit var binding: FragmentHomeShowtimeBinding
    private val viewModel : AdminViewModel by activityViewModels()

    private val DELAY_MS: Long = 1000
    private var searchJob: Job? = null

    private lateinit var adapterSearchMovie: SearchMovieAdapter
    private lateinit var adapterListShowTime: ListShowTimeAdapter

    private var isAddShowtime = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
                return inflater.inflate(R.layout.fragment_home_showtime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeShowtimeBinding.bind(view)

        binding.addButton.setOnClickListener {
            if (!isAddShowtime){
                binding.addMovieLayout.visibility = View.VISIBLE
                isAddShowtime = true
                setSearchAddInput()
            } else{
                binding.addMovieLayout.visibility = View.GONE
                isAddShowtime = false
            }
        }

        setAdapterListShowTime()

    }

    fun setSearchAddInput(){
        viewModel.getAllMovie()
        setAdapterAutoCpl()
        clickItemOnAutoComPlView()
    }

    private fun setAdapterAutoCpl(){
        binding.addMovieLayout.visibility = View.VISIBLE
        adapterSearchMovie = SearchMovieAdapter(requireContext())
        binding.movieNameAdd.setAdapter(adapterSearchMovie)
        viewModel.movies.observe(viewLifecycleOwner) {
            it?.let {
                adapterSearchMovie.submitList(it)
            }
        }
    }

    private fun clickItemOnAutoComPlView(){
        binding.movieNameAdd.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as MovieDTO
            binding.movieNameAdd.setText(null)
            val bundle = Bundle()
            bundle.putInt("id", selectedItem.id)
            bundle.putString("title", selectedItem.title)
            bundle.putString("posterUrl", selectedItem.posterUrl)
            bundle.putInt("duration", selectedItem.duration!!)
            Log.d("TAG", selectedItem.title)
            (activity as? AdminActivity)?.navigateToAddShowtime(bundle)
        }
    }

    private fun setAdapterListShowTime(){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        adapterListShowTime = ListShowTimeAdapter(requireContext()) { showtime ->
            clickItemAdapter(showtime)
        }
        binding.recyclerViewItems.adapter = adapterListShowTime
        viewModel.getShowTimes()
        viewModel.showTimes.observe(viewLifecycleOwner) { result ->
            result?.let {
                adapterListShowTime.submitList(result)
            }
        }
    }

    private fun clickItemAdapter(showtime : ShowTimeDTO){
        val bundle = Bundle()
        bundle.putInt("showtimeId", showtime.id)
        (activity as? AdminActivity)?.navigateToAddShowtime(bundle)
    }





}