package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class CouponDTO(
    @SerializedName("couponId")
    val id: Int,
    val name: String,
    val discount : Double,
    val expirationDate: String,
    val description: String,
    val cinemas : List<CinemaDTO>?,
    val code: String?,
    val amount: Int,
    val status: String?,
    val totalUsed: Int?
)