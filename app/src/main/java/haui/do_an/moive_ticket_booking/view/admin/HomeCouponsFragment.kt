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
import haui.do_an.moive_ticket_booking.adapter.admin.ListCouponAdapter
import haui.do_an.moive_ticket_booking.databinding.FragmentHomeCouponsBinding
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel


class HomeCouponsFragment : Fragment() {

    private lateinit var binding: FragmentHomeCouponsBinding
    private val viewModel: AdminViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_coupons, container, false)
        binding = FragmentHomeCouponsBinding.bind(view)
        viewModel.getAllCoupon()
        setupAdapterCoupon()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickBtnAddCoupon()

    }

    private fun setupAdapterCoupon() {
        val adapter = ListCouponAdapter(requireContext()){
            Log.d("TAG", "CouponId: ${it.id}")
        }
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItems.adapter = adapter
        viewModel.coupons.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun clickBtnAddCoupon(){
        binding.addButton.setOnClickListener {
            (activity as AdminActivity).navigateToAddCoupon()
        }
    }

}