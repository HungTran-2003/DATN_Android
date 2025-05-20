package haui.do_an.moive_ticket_booking.view.admin

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.FragmentAddCouponBinding
import haui.do_an.moive_ticket_booking.model.Coupon
import haui.do_an.moive_ticket_booking.viewmodel.AdminViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCouponFragment : Fragment() {

    private val viewModel : AdminViewModel by activityViewModels()


    private lateinit var binding: FragmentAddCouponBinding
    private val calendar = Calendar.getInstance()

    private lateinit var builder: AlertDialog.Builder
    private val selectedItems = mutableListOf<Int>()
    private var couponId: Int = 0
    private var selectDate: Boolean = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_coupon, container, false)
        binding = FragmentAddCouponBinding.bind(view)

        couponId = arguments?.getInt("couponId")?: 0
        val cinemaIds = arguments?.getIntegerArrayList("cinemaIds")
        if (couponId != 0) {

        }
        viewModel.getCinemas()
        setupDialogMutipleChoice()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickBtnSelectCinema()
        clickBtnSelectDate()
        clickBtnAddCoupon()
        clickBtnCancel()
        message()
    }

    private fun setupDialogMutipleChoice() {
        builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Chọn rạp")
        viewModel.cinemas.observe(viewLifecycleOwner) { cinemas ->
            cinemas?.let {
                val cinemaNames = it.map { it.name }.toTypedArray()
                val checkedItems = BooleanArray(cinemaNames.size)
                builder.setMultiChoiceItems(cinemaNames, checkedItems) { _, which, isChecked ->
                    if (isChecked) {
                        selectedItems.add(cinemas[which].id)
                    } else {
                        selectedItems.remove(cinemas[which].id)
                    }
                }
                builder.setPositiveButton("OK") { _, _ ->
                    val selectedGenres = selectedItems.map { cinemas[it] }
                    binding.tvSelectedCinema.text = selectedGenres.joinToString("\n") { it.name }
                }

                builder.setNegativeButton("Cancel", null)
            }
        }
    }

    private fun clickBtnSelectCinema(){
        binding.btnSelectCinema.setOnClickListener {
            builder.show()
        }
    }

    private fun clickBtnSelectDate() {
        binding.btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerTheme,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDateView() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        selectDate = true
        binding.tvSelectedDate.text = "Ngày: $formattedDate"
    }

    private fun clickBtnAddCoupon(){
        binding.btnSave.setOnClickListener {
            if (validateInput()){
                val coupon = Coupon(
                    name = binding.etCouponName.text.toString(),
                    discount = binding.etDiscount.text.toString().toDouble(),
                    expirationDate = binding.tvSelectedDate.text.toString().substring(6),
                    description = binding.etDescription.text.toString(),
                    cinemaIds = selectedItems,
                    amount = binding.etUsageLimit.text.toString().toInt()
                )
                viewModel.createCoupon(coupon)

            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        if (binding.etCouponName.text.isNullOrEmpty()) {
            binding.etCouponName.error = "Vui lòng nhập tên mã giảm giá"
            isValid = false
        }
        if (!selectDate) {
            binding.btnSelectCinema.error = "Vui lòng chọn ngày hết han"
            isValid = false
        }
        if (binding.etDiscount.text.isNullOrEmpty()){
            binding.etDiscount.error = "Vui lòng nhập phần trăm giảm giá"
            isValid = false
        }

        if (selectedItems.isEmpty()){
            binding.btnSelectCinema.error = "Vui lòng chọn rạp chiếu phim"
            isValid = false
        }

        if (binding.etDescription.text.isNullOrEmpty()){
            binding.etDescription.error = "Vui lòng nhập mô tả"
            isValid = false
        }

        if (binding.etUsageLimit.text.isNullOrEmpty()){
            binding.etUsageLimit.error = "Vui lòng nhập số lượng"
            isValid = false
        }

        return isValid
    }

    private fun clickBtnCancel(){
        binding.btnCancel.setOnClickListener {
            (activity as AdminActivity).backToHomeCoupon()
        }
    }

    private fun message(){
        viewModel.Message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                if (message.equals("Thêm mã giảm giá thành công")) {
                    (activity as? AdminActivity)?.backToHomeCoupon()
                    viewModel.clearData()
                }
            }
        }

    }

}