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
import haui.do_an.moive_ticket_booking.adapter.admin.ListCinemaAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeCinemasBinding
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel


class HomeCinemasFragment : Fragment() {

    private val viewModel: AdminViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeCinemasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_cinemas, container, false)
        binding = FragmentHomeCinemasBinding.bind(view)

        viewModel.getCinemas()
        setupAdapterCinema()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    private fun observeViewModel() {

    }

    private fun setupAdapterCinema(){
        val adapter = ListCinemaAdapter(){
            Log.d("CinemaID", it.id.toString())
        }
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItems.adapter = adapter

        viewModel.cinemas.observe(viewLifecycleOwner) { result ->
            result?.let {
                adapter.submitList(result)
                Log.d("adapt","cập nhật")
            }
        }
    }


}