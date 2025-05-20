package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.DTO.BookingDTO
import haui.do_an.moive_ticket_booking.api.BookingApiService
import haui.do_an.moive_ticket_booking.model.Booking
import javax.inject.Inject

class BookingRepository @Inject constructor(
    private val bookingApiService: BookingApiService
) {

    suspend fun addBooking(booking: Booking): Map<String, String> {
        val response = bookingApiService.createBooking(booking)
        if (!response.isSuccessful){
            throw Exception( response.errorBody()?.string().toString())
        }
        return response.body()?: throw Exception("Response body is null")
    }

    suspend fun getAllByUser(userId: Int) : List<BookingDTO>{
        val response = bookingApiService.getAllByUser(userId)
        if (!response.isSuccessful){
            return emptyList()
        }
        return response.body()?: emptyList()
    }

    suspend fun getUsed(userId: Int) : List<BookingDTO>{
        val response = bookingApiService.getUsed(userId)
        if (!response.isSuccessful){
            return emptyList()
        }
        return response.body()?: emptyList()
    }
}