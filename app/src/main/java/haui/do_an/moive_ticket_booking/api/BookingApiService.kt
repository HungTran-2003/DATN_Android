package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.BookingDTO
import haui.do_an.moive_ticket_booking.model.Booking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApiService  {
    @POST(ApiRoutes.Booking.ADD)
    suspend fun createBooking(@Body booking: Booking): Response<Map<String, String>>

    @GET(ApiRoutes.Booking.GET_BOOKING_NOT_SHOWING)
    suspend fun getAllByUser(@Path("userId") userId: Int): Response<List<BookingDTO>>

    @GET(ApiRoutes.Booking.GET_USED)
    suspend fun getUsed(@Path("userId") userId: Int): Response<List<BookingDTO>>
}