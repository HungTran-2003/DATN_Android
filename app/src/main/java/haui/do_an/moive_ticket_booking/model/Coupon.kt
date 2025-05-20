package haui.do_an.moive_ticket_booking.model

data class Coupon(
    val name: String,
    val discount : Double,
    val expirationDate: String,
    val description: String,
    val cinemaIds : List<Int>,
    val amount: Int
)
