package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.DTO.SeatDTO
import haui.do_an.moive_ticket_booking.DTO.ShowTimeDTO
import haui.do_an.moive_ticket_booking.DTO.TicketInfo
import haui.do_an.moive_ticket_booking.api.ShowTimeApiService
import haui.do_an.moive_ticket_booking.model.ShowTime
import javax.inject.Inject

class ShowTimeRepository @Inject constructor(
    private val showTimeApiService: ShowTimeApiService
) {
    suspend fun getShowTimes(): List<ShowTimeDTO> {
        val response = showTimeApiService.get()
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string().toString())
        }
        val result = sortShowTimes(response.body() ?: emptyList())
        return result
    }

    suspend fun addShowTime(showTime: ShowTime): String {
        val response = showTimeApiService.addShowTime(showTime)

        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string().toString())
        }
        return response.body() ?: "Error"
    }

    suspend fun getShowTime(id: Int): List<ShowTimeDTO> {
        val response = showTimeApiService.getShowTime(id)
        if (!response.isSuccessful) {
            throw Exception("có lỗi khi lấy thông tin lịch chiếu")
        }
        return response.body() ?: throw Exception("Error")
    }

    suspend fun getSeats(hallId: Int): List<SeatDTO> {
        val response = showTimeApiService.getSeats(hallId)
        if (!response.isSuccessful) {
            throw Exception("có lỗi khi lấy danh sách ghế")
        }
        return response.body() ?: throw Exception("Error")
    }

    suspend fun updateShowTime(id : Int, showTime: ShowTime): String {
        val response = showTimeApiService.updateShowTime(id, showTime)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string().toString())
        }
        return response.body() ?: "Error"
    }

    private fun sortShowTimes(showTimes: List<ShowTimeDTO>): List<ShowTimeDTO> {
        val priorityOrder = listOf("NOW_SHOWING", "COMING_SOON", "FINISHED_SHOWING")
        return showTimes.sortedWith(compareBy<ShowTimeDTO> { priorityOrder.indexOf(it.status) }.thenBy { it.startTime })
    }

    suspend fun getTicketInfo(showTimeId: Int, userId: Int): TicketInfo {
        val response = showTimeApiService.getTicketsByUser(showTimeId, userId)
        if (!response.isSuccessful) {
            throw Exception(response.errorBody()?.string().toString())
        }
        return response.body() ?: throw Exception("Error")

    }

}