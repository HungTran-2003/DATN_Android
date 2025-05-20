package haui.do_an.moive_ticket_booking.adapter.user

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import haui.do_an.moive_ticket_booking.DTO.CouponDTO
import haui.do_an.moive_ticket_booking.R
import haui.do_an.moive_ticket_booking.databinding.CustomItemCouponListBinding

class ListCouponAdapter (
    private val context: Context,
    private val hallId: Int,
    private val onItemClick: (CouponDTO, Boolean) -> Unit
) : ListAdapter<CouponDTO, ListCouponAdapter.CouponViewHolder>(CouponDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = CustomItemCouponListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class CouponViewHolder(private val binding: CustomItemCouponListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(coupon: CouponDTO) {

            binding.tvCouponName.text = coupon.name
            val qualified = coupon.cinemas!!.any { cinema ->
                cinema.halls!!.any { hall -> hall.id == hallId }
            }
            binding.tvCouponStatus.text = when (qualified) {
                true -> "Đủ điều kiện"
                else -> "Không đủ điều kiện"
            }

            val statusBackground = binding.tvCouponStatus.background as GradientDrawable

            val statusColor = when (qualified) {
                true -> R.color.status_now_showing
                else -> R.color.status_undetermined
            }
            statusBackground.setColor(ContextCompat.getColor(context, statusColor))

            binding.tvCouponCode.text = coupon.code

            binding.tvDiscount.text = "Giảm " + coupon.discount.toString() + "%"
            binding.tvExpiryDate.text = coupon.expirationDate
            binding.tvQuantityLabel.visibility = View.GONE
            binding.tvQuantity.visibility = View.GONE
            binding.root.setOnClickListener {
                onItemClick(coupon, qualified)
            }

            binding.ivCopy.setOnClickListener {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", coupon.code)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Đã copy: ${coupon.code}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    class CouponDiffCallback : DiffUtil.ItemCallback<CouponDTO>() {
        override fun areItemsTheSame(oldItem: CouponDTO, newItem: CouponDTO): Boolean {
            return oldItem.id == newItem.id // hoặc unique key của movie
        }

        override fun areContentsTheSame(oldItem: CouponDTO, newItem: CouponDTO): Boolean {
            return oldItem == newItem
        }
    }

}