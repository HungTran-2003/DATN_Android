package haui.do_an.moive_ticket_booking.adapter.admin

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.DTO.UserDTO
import haui.do_an.moive_ticket_booking.databinding.CustomListItemUserBinding
import haui.do_an.moive_ticket_booking.R

class ListUserAdapter (
    private val context: Context,
    private val onItemClick: (UserDTO) -> Unit
) : ListAdapter<UserDTO, ListUserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding =
            CustomListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class UserViewHolder(private val binding: CustomListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserDTO) {

            binding.userName.text = user.name;
            binding.userEmail.text = user.email
            binding.userExpense.text = "Đã chi: ${user.totalPay} VND"
            val statusBackground = binding.userStatus.background as GradientDrawable
            if (user.accountStatus == "ACTIVE") {
                binding.userStatus.text = "Hoạt động"
                statusBackground.setColor(ContextCompat.getColor(context, R.color.status_now_showing))
            } else {
                binding.userStatus.text = "Không hoạt động"
                statusBackground.setColor(ContextCompat.getColor(context, R.color.status_undetermined))
            }


            binding.root.setOnClickListener {
                onItemClick(user)
            }
        }
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserDTO>() {
        override fun areItemsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean {
            return oldItem.userId == newItem.userId
        }
        override fun areContentsTheSame(oldItem: UserDTO, newItem: UserDTO): Boolean {
            return oldItem == newItem
        }
    }
}
