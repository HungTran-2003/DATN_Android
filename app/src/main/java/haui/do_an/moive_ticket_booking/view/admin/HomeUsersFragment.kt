package haui.do_an.moive_ticket_booking.view.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.admin.ListUserAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeUsersBinding
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel

class HomeUsersFragment : Fragment() {

    private lateinit var binding: FragmentHomeUsersBinding
    private val viewModel : AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getAllUser()
        val view = inflater.inflate(R.layout.fragment_home_users, container, false)
        binding = FragmentHomeUsersBinding.bind(view)
        setupAdapter()
        return view
    }

    private fun observeData(){

    }

    private fun setupAdapter(){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListUserAdapter(requireContext()){

        }
        binding.recyclerViewItems.adapter = adapter
        viewModel.users.observe(viewLifecycleOwner){
            it?.let {
                adapter.submitList(it)
            }
        }
    }

}