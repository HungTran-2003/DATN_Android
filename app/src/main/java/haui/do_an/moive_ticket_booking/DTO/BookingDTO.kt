package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class BookingDTO(
    @SerializedName("bookingId")
    val id: Int,
    val userId: Int,
    val showtimeId: Int,
    val cinemaName: String,
    val startTime: String,
    val endTime: String,
    val movieName: String,
    val posterUrl: String,
    val hallName: String,
    val totalPrice: Double,
    val seats: List<SeatSelect>,
    val status: String

)

data class SeatSelect(
    val seatId: Int,
    val seatName: String
)

