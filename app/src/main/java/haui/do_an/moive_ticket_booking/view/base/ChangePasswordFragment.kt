package haui.do_an.moive_ticket_booking.view.base

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
import haui.do_an.moive_ticket_booking.databinding.FragmentChangePasswordBinding
import haui.do_an.moive_ticket_booking.view.auth.AuthActivity
import haui.do_an.moive_ticket_booking.view.user.UserActivity
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel

class ChangePasswordFragment : Fragment() {

    private var userId: Int = 0

    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel : UserViewModel by activityViewModels()

    private var function: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getInt("userId")?: 0
        function = arguments?.getString("function")
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangePasswordBinding.bind(view)
        observeData()

        clickBtnConfirm()

    }

    private fun observeData(){
        viewModel.Message.observe(viewLifecycleOwner){
            it?.let {
                val id = it.toIntOrNull()
                if (id != null) {
                    userId = id
                } else {
                    showToast(it)
                    if (function == "forgotPassword"){
                        (activity as AuthActivity).navigateToLogin()
                    } else {
                        (activity as UserActivity).backToHome()
                    }
                }
                viewModel.Message.postValue(null)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                showErrorDialog(it)
            }
        }
    }

    private fun clickBtnConfirm(){
        binding.btComfirm.setOnClickListener {
            val newPassword = binding.edNewPassword.text.toString()
            val confirmPassword = binding.edConfirm.text.toString()
            if (newPassword.isEmpty()){
                binding.edNewPassword.error = "Không được để trống"
            } else if (confirmPassword.isEmpty()){
                binding.edConfirm.error = "Không được để trống"
            } else if (newPassword != confirmPassword){
                binding.edConfirm.error = "Mật khẩu không khớp"
            } else {
                viewModel.changePassword(userId, newPassword)
            }
        }
    }
    private fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorDialog(error: String) {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(error)
        builder.setMessage("Vui lòng thử lại sau")
        Log.d("TAG", error)

        builder.setPositiveButton("Ok") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
        viewModel.clearErrorMessage()
    }
}

