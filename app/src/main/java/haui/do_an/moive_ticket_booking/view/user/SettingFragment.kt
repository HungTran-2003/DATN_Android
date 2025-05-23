package haui.do_an.moive_ticket_booking.view.user

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentSettingBinding
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        binding = FragmentSettingBinding.bind(view)

        binding.tvUserName.text = (activity as UserActivity).userName
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickHistory()
        clickProfile()
    }

    private fun clickHistory(){
        binding.lnHistory.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("modul", "history")
            (activity as UserActivity).navigateToBookingFragment(bundle)
        }
    }

    private fun clickProfile(){
        binding.lnAccount.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("function", "profile")
            val viewModelAuth : AuthViewModel by activityViewModels()
            val email = sharedPreferences.getString("email", "")
            if (email.isNullOrEmpty()){
                throw Exception("Có lỗi khi lấy email người dùng")
            }
            viewModelAuth.email = email
            viewModelAuth.getOTP()
            viewModelAuth.otpsend.observe(viewLifecycleOwner) {
                it?.let {
                    (activity as UserActivity).navigateToVerifyOTP(bundle)
                }
                viewModelAuth.clearMessage()
            }
            (activity as UserActivity).navigateToVerifyOTP(bundle)
        }
    }
}