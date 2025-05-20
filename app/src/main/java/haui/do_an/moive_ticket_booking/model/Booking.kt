package haui.do_an.moive_ticket_booking.model

data class Booking(
    val id: Long,
    val userId: Int,
    val showtimeId: Int,
    val totalPrice: Int,
    val seatIds: List<Int>,
    val couponCode: String?
)
