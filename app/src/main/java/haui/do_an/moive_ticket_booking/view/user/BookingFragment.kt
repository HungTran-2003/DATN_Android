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
import haui.do_an.moive_ticket_booking.adapter.user.ListBookingAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentBookingBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class BookingFragment : Fragment() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var binding: FragmentBookingBinding
    private val viewModel : UserViewModel by activityViewModels()

    private var userId = 0

    private var function: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)
        binding = FragmentBookingBinding.bind(view)
        userId = sharedPreferences.getInt("userId", 0)
        function = arguments?.getString("modul")
        if (function == "history"){
            viewModel.getUsed(userId)
        } else{
            viewModel.getBooking(userId)
        }
        setAdapterBooking()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel(){
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun setAdapterBooking(){
        val adapter = ListBookingAdapter(requireContext()) { booking ->
            val bundle = Bundle()
            bundle.putInt("bookingId", booking.id!!)
            if (function == "history"){
                (activity as UserActivity).navigateToReviewFragment(bundle)
            } else{
                (activity as UserActivity).navigateToPaymentComfirm(bundle)
            }


        }

        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItems.adapter = adapter
        viewModel.bookings.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")
        Log.d("TAG", error)

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToHome()
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()
    }
}