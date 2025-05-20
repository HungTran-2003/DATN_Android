package haui.do_an.moive_ticket_booking.DTO

data class UserDTO(
    val userId: Int,
    val name: String,
    val email: String,
    val phoneNumber : String,
    val registrationDate: String,
    val accountStatus: String,
    val totalPay: Int
)
