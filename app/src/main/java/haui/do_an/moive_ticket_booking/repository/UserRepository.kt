package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.DTO.UserDTO
import haui.do_an.moive_ticket_booking.api.UserAPIService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userApiService: UserAPIService
) {
    suspend fun createUser(userName: String, email: String, password: String): String {
        val map = mapOf(
            "username" to userName,
            "email" to email,
            "password" to password
        )
        val response = userApiService.addUser(map)
        return response.body().toString()
    }

    suspend fun login(userName: String, password: String): Map<String, String> {
        val map = mapOf(
            "email" to userName,
            "password" to password
        )
        val response = userApiService.login(map)
        return response.body() ?: emptyMap()
    }

    suspend fun addFavourite(userId: Int, movieId: Int): String {
        val map = mapOf(
            "userId" to userId,
            "movieId" to movieId
        )
        val response = userApiService.addFavourite(map)
        if (response.isSuccessful)
            return response.body().toString()
        else
            throw Exception("Lỗi khi thêm phim yêu thích")
    }

    suspend fun deleteFavourite(userId: Int, movieId: Int): String {

        val response = userApiService.deleteFavourite(userId, movieId)
        if (response.isSuccessful)
            return response.body().toString()
        else
            throw Exception("Lỗi khi xoá phim yêu thích")
    }

    suspend fun getAllUser(): List<UserDTO> {
        val response = userApiService.getAllUser()
        if (response.isSuccessful)
            return response.body() ?: emptyList()
        else
            throw Exception(response.errorBody()?.string().toString())
    }


}