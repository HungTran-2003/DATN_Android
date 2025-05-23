package haui.do_an.moive_ticket_booking.view.auth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentRegisterBinding
import haui.do_an.moive_ticket_booking.utils.Validatior
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel
import androidx.fragment.app.activityViewModels

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val sharedViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRegisterBinding.bind(view)

        binding.login.setOnClickListener {
            (activity as AuthActivity).navigateToLogin()
        }

        binding.password.setOnTouchListener { v, event ->
            handlePasswordToggleTouch(binding.password, event)
        }

        binding.confirmPassword.setOnTouchListener { v, event ->
            handlePasswordToggleTouch(binding.confirmPassword, event)
        }

        binding.signUpButton.setOnClickListener {
            signUp()
        }
        // test
        sharedViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
        //test
        sharedViewModel.registerSuccess.observe(viewLifecycleOwner) { loginSuccess ->
            Toast.makeText(requireContext(), loginSuccess, Toast.LENGTH_SHORT).show()
            (activity as AuthActivity).navigateToLogin()
        }



    }

    private fun signUp() {
        val name = binding.username.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        } else if (!Validatior().isValidEmail(email.toString())){
            Toast.makeText(requireContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show()
        } else if (!Validatior().isValidPassword(password.toString())){
            Toast.makeText(requireContext(), "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa và số", Toast.LENGTH_SHORT).show()
        } else if (password.toString() != confirmPassword.toString()){
            Toast.makeText(requireContext(), "Mật khẩu không khớp", Toast.LENGTH_SHORT).show()
        } else {
            sharedViewModel.saveTermUser(name, password, email)
            val bundle = Bundle()
            bundle.putString("function", "register")
            (activity as AuthActivity).navigateToVerifyOTP(bundle)
            sharedViewModel.createUser()
        }
    }

    private fun handlePasswordToggleTouch(passwordEditText: EditText, event: MotionEvent): Boolean {
        val DRAWABLE_RIGHT = 2 // Vị trí của biểu tượng drawableEnd

        if (event.action == MotionEvent.ACTION_UP) {
            if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                if (passwordEditText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                    passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0)
                } else {
                    passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_on, 0)
                }
                passwordEditText.setSelection(passwordEditText.text.length) // Giữ con trỏ ở cuối
                return true
            }
        }
        return false
    }

}