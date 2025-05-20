package haui.do_an.moive_ticket_booking.DTO

data class TicketDTO(
    val seat: Seat,
    val posterUrl: String,
    val price: Double,
    val cinemaName: String,
    val startTime: String,
    val endTime: String,
    val hallName: String,
    val ticketCode: String,
    val movieName: String,
    val ticketId: Int
)

data class Seat(
    val number: String,
    val row: String
)