package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.DTO.CouponDTO
import haui.do_an.moive_ticket_booking.api.CouponService
import haui.do_an.moive_ticket_booking.model.Coupon
import javax.inject.Inject

class CouponRepository @Inject constructor(
    private val couponService: CouponService
) {

    suspend fun createCoupon(coupon: Coupon): String {
        val response = couponService.createCoupon(coupon)
        if (response.isSuccessful) {
            return response.body() ?: "Coupon created successfully"
        } else {
            return response.errorBody()?.string().toString()
        }
    }

    suspend fun getAll(): List<CouponDTO> {
        val response = couponService.getAll()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string().toString())
        }
    }

    suspend fun getAllByUser(userId: Int): List<CouponDTO> {
        val response = couponService.getAllByUser(userId)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception(response.errorBody()?.string().toString())
        }
    }
}