package haui.do_an.moive_ticket_booking.DTO

data class UserDTO(
    val userId: Int,
    val name: String,
    var email: String,
    var phoneNumber : String,
    val registrationDate: String,
    val accountStatus: String,
    val totalPay: Int
)
