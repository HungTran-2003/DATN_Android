package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.SeatDTO
import haui.do_an.moive_ticket_booking.DTO.TicketDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TicketApiService {

    @GET(ApiRoutes.Ticket.GET_SEAT_BY_SHOWTIME)
    suspend fun getSeatsByShowTimeId(@Path("showtimeId") showtimeId: Int): Response<List<SeatDTO>>

    @GET(ApiRoutes.Ticket.GET_ALL)
    suspend fun getAll(@Path("userId") userId: Int, @Path("bookingId") bookingId: Int): Response<List<TicketDTO>>

}