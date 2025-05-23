package haui.do_an.moive_ticket_booking.view.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentForgotPasswordBinding
import haui.do_an.moive_ticket_booking.utils.Validatior
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel


class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentForgotPasswordBinding.bind(view)

        clickBack()
        clickBtnRegistration()
        clickSendCode()

        observe()
    }

    private fun clickBack(){
        binding.imbtBack.setOnClickListener {
            (activity as AuthActivity).navigateToLogin()
        }

        binding.tvBacktoLogin.setOnClickListener {
            (activity as AuthActivity).navigateToLogin()
        }
    }

    private fun clickBtnRegistration(){
        binding.btRegister.setOnClickListener {
            (activity as AuthActivity).navigateToRegister()
        }
    }

    private fun clickSendCode(){
        binding.btForgotPW.setOnClickListener {
            val email = binding.passwordEditText.text.toString()
            val validatior = Validatior()
            if(email.isNotEmpty()){
                if(validatior.isValidEmail(email)){
                    viewModel.email = email
                    viewModel.getOTP()
                    Log.d("gửi","fragment" )
                    binding.viewSwitcher.displayedChild = 1
                } else {
                    binding.passwordEditText.error = "Email nhập không hợp lệ"
                }
            } else {
                binding.passwordEditText.error = "Vui lòng nhập email"
            }
        }
    }

    private fun observe(){
        viewModel.otpsend.observe(viewLifecycleOwner) {
            it?.let {
                binding.viewSwitcher.displayedChild = 0
                val bundle = Bundle()
                bundle.putString("function", "forgotPassword")
                viewModel.clearMessage()
                (activity as AuthActivity).navigateToVerifyOTP(bundle)
            }
        }
    }
}