package haui.do_an.moive_ticket_booking.model

data class ShowTime(
    private val id: Int? = null,
    private val movieId: Int,
    private val cinemaId: Int,
    private val hallId: Int,
    private val startTime: String,
    private val endTime: String,
    private val price: Double,
    private val date: String,
    private val maxTicket: Int
)
