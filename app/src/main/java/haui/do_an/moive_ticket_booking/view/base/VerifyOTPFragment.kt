package haui.do_an.moive_ticket_booking.view.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentVerifyOTPBinding
import haui.do_an.moive_ticket_booking.view.auth.AuthActivity
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel

class VerifyOTPFragment: Fragment() {

    private val viewModel: AuthViewModel by activityViewModels()

    private lateinit var binding: FragmentVerifyOTPBinding


    private var otpFailed: Int = 0
    private var otpTryCount: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_o_t_p, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentVerifyOTPBinding.bind(view)

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        viewModel.otpsend.observe(viewLifecycleOwner) { otpSend ->
            Toast.makeText(requireContext(), otpSend, Toast.LENGTH_SHORT).show()
        }

        viewModel.registerSuccess.observe(viewLifecycleOwner) { loginSuccess ->
            Toast.makeText(requireContext(), loginSuccess, Toast.LENGTH_SHORT).show()
            (activity as AuthActivity).navigateToLogin()
        }

        binding.confirmButton.setOnClickListener {
            clickConfirm()
        }

        binding.backButton.setOnClickListener {
            (activity as AuthActivity).backToRegister()
        }

        viewModel.getOTP()
    }

    private fun clickConfirm() {
        println("clickConfirm")
        val cell1 = binding.cell1.text.toString()
        val cell2 = binding.cell2.text.toString()
        val cell3 = binding.cell3.text.toString()
        val cell4 = binding.cell4.text.toString()

        if (cell1.isEmpty() || cell2.isEmpty() || cell3.isEmpty() || cell4.isEmpty()){
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ mã OTP", Toast.LENGTH_SHORT).show()
        }
        else {
            val userOTP = cell1 + cell2 + cell3 + cell4
            viewModel.sendOtp(userOTP)
            viewModel.otpConfirm.observe(viewLifecycleOwner) { otpConfirm ->
                if (otpConfirm == true) {
                    viewModel.createUser()
                } else {
                    Toast.makeText(requireContext(), "Mã OTP không đúng hoặc đã hết hạn", Toast.LENGTH_SHORT).show()
                    otpFailed++
                    if (otpFailed == 4){
                        Toast.makeText(requireContext(), "Bạn đã nhập sai quá 4 lần, chúng tôi đã gửi lại mã cho bạn", Toast.LENGTH_SHORT).show()
                        otpFailed = 0
                        viewModel.getOTP()
                        otpTryCount++
                        if (otpTryCount == 3){
                            Toast.makeText(requireContext(), "Bạn đã vựt quá giới hạn gửi mã OTP, vui lòng thử lại sau", Toast.LENGTH_SHORT).show()
                            (activity as AuthActivity).navigateToLogin()
                        }
                    }
                }
            }
        }
    }


}