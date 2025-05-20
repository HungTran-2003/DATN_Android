package haui.do_an.moive_ticket_booking.api

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

import javax.inject.Inject


class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // Kiểm tra nếu là API đăng nhập thì không thêm token
        val excludedUrls = listOf(
            "/api/users/login",
            "/api/users/add",
            "/api/otp",
            "/api/otp/verify-otp",
            "/api/cities",
            ApiRoutes.Movie.IMAGE
        )

        // Kiểm tra nếu URL yêu cầu không thêm token
        if (excludedUrls.any { request.url.toString().contains(it) }) {
            return chain.proceed(request)
        }

        val token = sharedPreferences.getString("token", null)
        val requestBuilder = request.newBuilder()

        // Nếu có token thì thêm vào header
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}
