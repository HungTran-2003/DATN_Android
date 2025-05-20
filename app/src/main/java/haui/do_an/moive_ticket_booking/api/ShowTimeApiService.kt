package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.SeatDTO
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.DTO.TicketInfo
import haui.do_an.moive_ticket_booking.model.ShowTime
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShowTimeApiService {

    @POST(ApiRoutes.ShowTime.ADD)
    suspend fun addShowTime(@Body showTime: ShowTime): Response<String>

    @GET(ApiRoutes.ShowTime.ALL)
    suspend fun get(): Response<List<ShowTimeDTO>>

    @GET(ApiRoutes.ShowTime.DETAIL)
    suspend fun getShowTime(@Path("id") id: Int): Response<List<ShowTimeDTO>>

    @GET(ApiRoutes.ShowTime.GET_SEATS)
    suspend fun getSeats(@Path("hallId") hallId: Int): Response<List<SeatDTO>>

    @PUT(ApiRoutes.ShowTime.DETAIL)
    suspend fun updateShowTime(@Path("id") id: Int, @Body showTime: ShowTime): Response<String>

    @GET(ApiRoutes.ShowTime.GET_TICKETS_BY_USER)
    suspend fun getTicketsByUser(@Path("showtimeId") showtimeId: Int, @Path("userId") userId: Int):
            Response<TicketInfo>




}