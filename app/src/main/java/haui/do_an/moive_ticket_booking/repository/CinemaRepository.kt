package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.api.CinemaApiService
import haui.do_an.moive_ticket_booking.DTO.CinemaDTO
import haui.do_an.moive_ticket_booking.DTO.CityDTO
import haui.do_an.moive_ticket_booking.DTO.HallDTO
import javax.inject.Inject

class CinemaRepository @Inject constructor(private val cinemaApiService: CinemaApiService) {

    suspend fun getCitys() : List<CityDTO> {
        val response = cinemaApiService.getAllCities()
        return response.body() ?: emptyList()
    }

    suspend fun getAllCinema() : List<CinemaDTO> {
        val response = cinemaApiService.getAllCinemas()
        if (!response.isSuccessful) {
            throw Exception("Lỗi khi tải danh sách rạp chiếu")
        }
        return response.body() ?: emptyList()
    }

    suspend fun getHall(cinemaId: Int): List<HallDTO> {
        val response = cinemaApiService.getHall(cinemaId)
        if (!response.isSuccessful) {
            throw Exception("Lỗi khi tải danh sách phòng chiếu")
        }
        return response.body() ?: emptyList()

    }

    suspend fun getHallByShowtime(showtimeId: Int): HallDTO {
        val response = cinemaApiService.getHallByShowtime(showtimeId)
        if (!response.isSuccessful) {
            throw Exception("Lỗi khi tải dữ liệu phòng chiếu")
        }
        return response.body() ?: HallDTO(0,"",0)
        }

}