package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class ShowTimeDTO (
    val id: Int,
    val hallId: Int,
    val hallName: String?,
    val movieId: Int?,
    val movieName: String?,
    val startTime: String?,
    val endTime: String?,
    val status: String?,
    @SerializedName("bookingCount")
    val numberSoldTickets: Int?,
    val posterUrl: String?,
    val cinemaName: String?,
    val ticketPrice: Double?,
    val duration: Int?
)

data class TicketInfo(
    val ticketCount: Int,
    val maxTicket : Int
)

