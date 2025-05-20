package haui.do_an.moive_ticket_booking.adapter.user

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.databinding.ItemTimeBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListDateAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<String, ListDateAdapter.DateViewHolder>(MovieDiffCallback()) {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = ItemTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        holder.bind(getItem(position), position == selectedPosition)
    }

    override fun submitList(list: List<String>?) {
        super.submitList(list)
        // Đặt lại selectedPosition về 0 và thông báo cho item đầu tiên nếu có
        selectedPosition = 0
        if (!list.isNullOrEmpty()) {
            // Đảm bảo chọn item đầu tiên và gọi callback
            onItemClick(list[0])
        }
    }

    override fun getItemCount(): Int = currentList.size

    inner class DateViewHolder(private val binding: ItemTimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(daterow: String, isSelected: Boolean) {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd-MM", Locale.getDefault())
            val date = inputFormat.parse(daterow)
            binding.tvTime.text = outputFormat.format(date!!)

            if (isSelected) {
                binding.cardViewTime.setCardBackgroundColor(Color.parseColor("#4F82FF"))
            } else {
                binding.cardViewTime.setCardBackgroundColor(Color.WHITE)
            }

            binding.root.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    // Lưu vị trí cũ
                    val previousSelected = selectedPosition
                    // Cập nhật vị trí mới
                    selectedPosition = adapterPosition

                    // Chỉ cập nhật UI nếu thực sự có sự thay đổi
                    if (previousSelected != selectedPosition) {
                        // Thông báo cập nhật cho item cũ và mới
                        notifyItemChanged(previousSelected)
                        notifyItemChanged(selectedPosition)

                        // Gọi callback với item được chọn mới
                        onItemClick(daterow)
                    }
                }
            }
        }
    }

    class MovieDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}