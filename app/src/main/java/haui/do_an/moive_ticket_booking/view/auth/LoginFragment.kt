package haui.do_an.moive_ticket_booking.view.auth

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentLoginBinding
import haui.do_an.moive_ticket_booking.utils.Validatior
import haui.do_an.moive_ticket_booking.viewmodel.AuthViewModel
import androidx.core.content.edit
import dagger.hilt.android.AndroidEntryPoint
import haui.do_an.moive_ticket_booking.view.admin.AdminActivity
import haui.do_an.moive_ticket_booking.view.user.UserActivity
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment: Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: AuthViewModel by activityViewModels()
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.register.setOnClickListener {
            clickRegister()
        }

        binding.loginButton.setOnClickListener {
            clickLogin()
        }

        binding.passwordEditText.setOnTouchListener { v, event ->
            handlePasswordToggleTouch(binding.passwordEditText, event)
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { loginSuccess ->
            if (loginSuccess.isNotEmpty()) {
                loginSuccess(loginSuccess)
            } else {
                Toast.makeText(requireContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putBoolean("saveAccount", false).apply()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            println(errorMessage)
        }

        saveAccount()
        clickForgotPassword()
    }

    private fun clickRegister(){
        (activity as? AuthActivity)?.navigateToRegister()
    }

    private fun clickForgotPassword(){
        binding.forgotPasswordTextView.setOnClickListener {
            (activity as AuthActivity).navigateToForgotPassword()
        }
    }

    private fun clickGoogleLogin(){

    }

    private fun clickLogin() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        when {
            email.isEmpty() || password.isEmpty() -> {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng nhập đầy đủ thông tin",
                    Toast.LENGTH_SHORT
                ).show()
            }

            !Validatior().isValidEmail(email) -> {
                Toast.makeText(requireContext(), "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            }

            !Validatior().isValidPassword(password) -> {
                Toast.makeText(
                    requireContext(),
                    "Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa và số",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                viewModel.loginUser(email, password)
            }
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

    private fun saveAccount(){
        binding.saveAccountCheckbox.isChecked = sharedPreferences.getBoolean("saveAccount", false)
        if (binding.saveAccountCheckbox.isChecked){
            binding.emailEditText.setText(sharedPreferences.getString("email", ""))
            binding.passwordEditText.setText(sharedPreferences.getString("password", ""))
            viewModel.loginUser(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())

        }
    }

    private fun loginSuccess(result : Map<String, String>){
        Toast.makeText(requireContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT)
            .show()
        sharedPreferences.edit()
            .putString("token", result["token"])
            .putInt("userId", result["userId"]!!.toInt())
            .putString("role", result["role"])
            .apply()
        if (binding.saveAccountCheckbox.isChecked){
            sharedPreferences.edit()
                .putString("email", binding.emailEditText.text.toString())
                .putString("password", binding.passwordEditText.text.toString())
                .putBoolean("saveAccount", true)
                .apply()
        }
        when(result["role"]){
            "USER" -> {
                val intent = Intent(requireContext(), UserActivity::class.java)

                intent.putExtra("username", result["username"])
                startActivity(intent)
                requireActivity().finish()
            }
            "ADMIN" -> {
                val intent = Intent(requireContext(), AdminActivity::class.java)
                intent.putExtra("username", result["username"])
                startActivity(intent)
                requireActivity().finish()
            }
            else -> {
                Toast.makeText(requireContext(), "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putBoolean("saveAccount", false).apply()
            }
        }
    }
}