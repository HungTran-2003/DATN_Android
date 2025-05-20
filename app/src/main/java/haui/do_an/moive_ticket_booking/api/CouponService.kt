package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.CouponDTO
import haui.do_an.moive_ticket_booking.model.Coupon
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CouponService {

    @POST(ApiRoutes.Coupon.ADD)
    suspend fun createCoupon(@Body coupon: Coupon): Response<String>

    @GET(ApiRoutes.Coupon.ALL)
    suspend fun getAll(): Response<List<CouponDTO>>

    @GET(ApiRoutes.Coupon.ALL_BY_USER)
    suspend fun getAllByUser(@Path("userId") userId: Int): Response<List<CouponDTO>>
}