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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.user.ListTicketAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentTicketBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import javax.inject.Inject
import kotlin.getValue

@AndroidEntryPoint
class TicketFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: UserViewModel by activityViewModels()
    private lateinit var binding: FragmentTicketBinding

    private var bookingId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket, container, false)
        binding = FragmentTicketBinding.bind(view)
        val userId = sharedPreferences.getInt("userId", 0)
        bookingId = arguments?.getInt("bookingId") ?: 0

        viewModel.getTicket(userId, bookingId)

        setAdapterTicket()
        observeViewModel()

        return view
    }

    private fun observeViewModel(){
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun setAdapterTicket(){
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        val adapter = ListTicketAdapter(requireContext()) {

        }

        binding.recyclerViewItems.adapter = adapter
        viewModel.tickets.observe(viewLifecycleOwner) {
            Log.d("TAG", "đã nhận")
            adapter.submitList(it)
        }

    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")
        Log.d("TAG", error)

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToLogin()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

}