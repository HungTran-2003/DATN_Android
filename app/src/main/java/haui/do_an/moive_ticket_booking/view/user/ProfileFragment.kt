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
import haui.do_an.moive_ticket_booking.databinding.FragmentProfileBinding
import haui.do_an.moive_ticket_booking.utils.Validatior
import haui.do_an.moive_ticket_booking.viewmodel.UserViewModel

class ProfileFragment : Fragment() {

    val viewModel: UserViewModel by activityViewModels()

    lateinit var binding: FragmentProfileBinding
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        observe()
        clickBtnUpdate()
        clickBtnBack()
    }

    private fun observe(){
        viewModel.profile.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it.name
            binding.etEmail.setText(it.email)
            it.phoneNumber?.let {
                binding.etPhoneNumber.setText(it)
            }
            binding.switcher.displayedChild = 1
        }

        viewModel.Message.observe(viewLifecycleOwner){
            if (it != null){
                binding.switcher.displayedChild = 1
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.switcher.displayedChild = 1
                showErrorDialog(it)
            }
        }
    }

    private fun clickBtnUpdate(){
        binding.btnSaveChanges.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            var error = false
            val validatior = Validatior()
            if (email.isEmpty()){
                binding.etEmail.error = "Email không được để trống"
                error = true
            } else if (!validatior.isValidEmail(email)){
                binding.etEmail.error = "Email không hợp lệ"
                error = true
            }
            if (phoneNumber.isEmpty()){
                error = true
                binding.etPhoneNumber.error = "Số điện thoại không được để trống"
            }
            if (!error) {
                val userDTO = viewModel.profile.value
                userDTO?.email = email
                userDTO?.phoneNumber = phoneNumber
                viewModel.updateProfile(userDTO!!)
                binding.switcher.displayedChild = 0
            }
        }
    }

    private fun clickBtnBack(){
        binding.btnBack.setOnClickListener {
            (activity as UserActivity).backToSetting()
        }
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