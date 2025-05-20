package haui.do_an.moive_ticket_booking.repository


import haui.do_an.moive_ticket_booking.DTO.TicketDTO
import haui.do_an.moive_ticket_booking.api.TicketApiService
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketApiService: TicketApiService
) {
    suspend fun getSeatReserved(showtimeId: Int): List<Int> {
        val response = ticketApiService.getSeatsByShowTimeId(showtimeId)
        if (response.isSuccessful) {
            if (response.body() != null) {
                return response.body()!!.map { it.id!! }
            }
            return List(1) { 0 }
        } else {
            throw Exception("Failed to fetch seats")
        }
    }

    suspend fun getAll(userId: Int, bookingId: Int): List<TicketDTO> {
        val response = ticketApiService.getAll(userId, bookingId)
        if (response.isSuccessful) {
            if (response.body() != null) {
                return response.body()!!
            }
            return emptyList()
        } else {
            throw Exception("Failed to fetch seats")
        }
    }


}