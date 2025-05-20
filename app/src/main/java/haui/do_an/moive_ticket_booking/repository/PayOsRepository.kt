package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.DTO.PayOsDTO
import haui.do_an.moive_ticket_booking.api.PayOsService
import javax.inject.Inject

class PayOsRepository @Inject constructor(
    private val payOsService: PayOsService
) {

    suspend fun createLinkPayment(request: Map<String, Any>): String {
        val response = payOsService.getLinkPayment(request)

        if (response.message == "success") {
            return response.data!!.get("checkoutUrl").toString()
        } else {
            throw Exception("Lỗi khi tạo link thanh toán")
        }
    }
}