package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.CinemaDTO
import haui.do_an.moive_ticket_booking.DTO.CityDTO
import haui.do_an.moive_ticket_booking.DTO.HallDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CinemaApiService {
    @GET(ApiRoutes.City.ALL)
    suspend fun getAllCities(): Response<List<CityDTO>>

    @GET(ApiRoutes.Cinema.ALL)
    suspend fun getAllCinemas(): Response<List<CinemaDTO>>

    @GET(ApiRoutes.Cinema.GET_HALL)
    suspend fun getHall(@Path("cinemaId") cinemaId: Int): Response<List<HallDTO>>

    @GET(ApiRoutes.Cinema.GET_HALL_BY_SHOWTIME)
    suspend fun getHallByShowtime(@Path("showtimeId") showtimeId: Int): Response<HallDTO>

}