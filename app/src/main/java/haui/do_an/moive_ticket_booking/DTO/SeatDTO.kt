package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class SeatDTO(
    @SerializedName("seatId")
    val id: Int?,
    val rowNumbers: String?,
    val seatNumbers: String?,
    val typeSeat: String?
)
