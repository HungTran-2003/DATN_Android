package haui.do_an.moive_ticket_booking.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import haui.do_an.moive_ticket_booking.DTO.Genres
import haui.do_an.moive_ticket_booking.api.MoiveAPIService
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApiService: MoiveAPIService
) {
    suspend fun addMovie(movie: MovieTmdbDTO): String {
        val response = movieApiService.addMovie(movie)
        if (!response.isSuccessful){
            return response.errorBody()?.string().toString()
        }
        return response.body().toString()
    }

    suspend fun getAll() : List<MovieDTO>{
        val response = movieApiService.getAll()
        if (!response.isSuccessful){
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun searchMovie(searchText: String) : List<MovieDTO>{
        val response = movieApiService.searchMovie(searchText)
        if (!response.isSuccessful){
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun getMovieShowing() : List<MovieDTO>{
        val response = movieApiService.getMovieShowing()
        if (!response.isSuccessful){
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun getMovieComing() : List<MovieDTO>{
        val response = movieApiService.getMovieComing()
        if (!response.isSuccessful){
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun getMovieSpecial() : List<MovieDTO>{
        val response = movieApiService.getMovieSpecial()
        if (!response.isSuccessful){
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun getDetailMovie(id: Int, userId: Int): MovieDTO {
        val response = movieApiService.getMovieDetail(userId, id)

        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        }
        throw Exception("Lỗi khi tải dữ liệu phim")
    }

    suspend fun updateMovie(
        id: Int,
        type: String,
        status: String,
        uriPoster: Uri?,
        uriBanner: Uri?,
        context: Context
    ) : String {
        val response = movieApiService.updateMovie(
            createImageMultipart(uriPoster, context, "imagePoster"),
            createImageMultipart(uriBanner, context, "imageBanner"),
            id.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            type.toRequestBody("text/plain".toMediaTypeOrNull()),
            status.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        if (!response.isSuccessful){
            throw Exception("Lỗi khi cập nhật dữ liệu phim")
        }
        return response.body().toString()

    }

    private fun createImageMultipart(imageUri: Uri?,context: Context, name: String): MultipartBody.Part? {
        return imageUri?.let {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.jpg")

            inputStream.use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData(name, file.name, requestFile)
        }
    }

    suspend fun getAllGenre() : List<Genres>{
        val response = movieApiService.getAllGenre()
        if (!response.isSuccessful){
            throw Exception(response.errorBody()?.string())
        }
        if (response.body() == null){
            throw Exception("Lỗi khi tải danh sách thể loại")
        }
        return response.body()!!
    }

    suspend fun filterMovie(
        searchText: String?,
        type: String?,
        year: Int?,
        status: String?,
        genres: List<Int>?) : List<MovieDTO> {
        val response = movieApiService.filterMovie(searchText, type, year, status, genres)
        Log.d("filter", "đã vào repon")
        if (!response.isSuccessful) {
            throw Exception("Lỗi khi tải danh sách phim")
        }
        return response.body() ?: emptyList()
    }

    suspend fun deleteMovie(id: Int): String{
        val response = movieApiService.deleteMovie(id)
        if (!response.isSuccessful){
            throw Exception("Lỗi khi xóa phim")
        }
        return response.body() ?: throw Exception("Lỗi khi xóa phim")
    }
}