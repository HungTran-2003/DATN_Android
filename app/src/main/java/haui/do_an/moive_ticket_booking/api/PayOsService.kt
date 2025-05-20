package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.PayOsDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PayOsService {

    @POST(ApiRoutes.PayOs.GET_LINK)
    suspend fun getLinkPayment(@Body request: Map<String, @JvmSuppressWildcards Any>) : PayOsDTO
}