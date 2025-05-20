package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserAPIService {

    @POST(ApiRoutes.User.ADD)
    suspend fun addUser(@Body map: Map<String, String>): Response<String>

    @POST(ApiRoutes.User.LOGIN)
    suspend fun login(@Body map: Map<String, String>): Response<Map<String, String>>

    @POST(ApiRoutes.User.FAVOURITE)
    suspend fun addFavourite(@Body map: Map<String, Int>): Response<String>

    @DELETE(ApiRoutes.User.FAVOURITE)
    suspend fun deleteFavourite(@Query("userId") userId: Int, @Query("movieId") movieId: Int): Response<String>

    @GET(ApiRoutes.User.GETALL)
    suspend fun getAllUser(): Response<List<UserDTO>>

}