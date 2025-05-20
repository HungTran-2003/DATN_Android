package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.api.OtpAPIService
import okhttp3.RequestBody
import java.util.Objects
import javax.inject.Inject

class OtpReponsitory @Inject constructor(
    private val otpApiService: OtpAPIService
) {
    suspend fun getOtp(email: String): String{
        val response = otpApiService.getOTP(email)
        return response.body().toString()
    }

    suspend fun sendOtp(map: Map<String, String>): Boolean{
        val response = otpApiService.sendOTP(map)
        return response.isSuccessful
    }
}