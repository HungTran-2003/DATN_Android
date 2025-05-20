package haui.do_an.moive_ticket_booking.view.user

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.adapter.user.ListCouponAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentChoseCouponBinding
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel


class ChoseCouponFragment : Fragment() {

    private lateinit var binding: FragmentChoseCouponBinding
    private val viewModel: UserViewModel by activityViewModels()

    private var hallId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chose_coupon, container, false)
        binding = FragmentChoseCouponBinding.bind(view)
        hallId = arguments?.getInt("hallId")?: 0
        Log.d("hallId", hallId.toString())
        val userId = arguments?.getInt("userId")?: 0

        viewModel.getCoupon(userId)
        setupAdapter()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        clickBtnBack()
    }

    private fun observeViewModel(){
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun setupAdapter() {
        val adapter = ListCouponAdapter(requireContext(), hallId){ coupon, qualified ->
            if (qualified){
                viewModel.saveCode(coupon.code.toString(), coupon.discount)
                (activity as UserActivity).backToPaymentComfirm()
            } else {
                Toast.makeText(requireContext(), "Bạn không đủ điều kiện dùng mã này", Toast.LENGTH_SHORT).show()
            }

        }

        binding.rVCoupon.adapter = adapter
        viewModel.coupons.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            Log.d("adapter", "đã cập nhật")
        }
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")

        builder.setPositiveButton("Ok") { dialog, which ->
            (activity as UserActivity).backToPaymentComfirm()
            dialog.dismiss()
        }
        Log.d("error", error)
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()
    }

    private fun clickBtnBack(){
        binding.btnBack.setOnClickListener {
            (activity as UserActivity).backToPaymentComfirm()
        }
    }

}