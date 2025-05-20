package haui.do_an.moive_ticket_booking.api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OtpAPIService {
    @POST(ApiRoutes.Base.GET_OTP)
    suspend fun getOTP(@Body body: String): Response<String>

    @POST(ApiRoutes.Base.SEND_OTP)
    suspend fun sendOTP(@Body body: Map<String, String>): Response<Boolean>


}