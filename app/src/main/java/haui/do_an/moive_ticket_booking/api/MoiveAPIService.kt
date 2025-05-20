package haui.do_an.moive_ticket_booking.api

import android.graphics.Movie
import haui.do_an.moive_ticket_booking.DTO.Genres
import haui.do_an.moive_ticket_booking.DTO.MovieDTO
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MoiveAPIService {

    @POST(ApiRoutes.Movie.ADD)
    suspend fun addMovie(@Body movie: MovieTmdbDTO): Response<String>

    @GET(ApiRoutes.Movie.ALL)
    suspend fun getAll(): Response<List<MovieDTO>>

    @GET(ApiRoutes.Movie.SEARCH)
    suspend fun searchMovie(@Query("searchText") searchText: String): Response<List<MovieDTO>>

    @GET(ApiRoutes.Movie.MOVIE_SHOWING)
    suspend fun getMovieShowing(): Response<List<MovieDTO>>

    @GET(ApiRoutes.Movie.MOVIE_COMING)
    suspend fun getMovieComing(): Response<List<MovieDTO>>

    @GET(ApiRoutes.Movie.SPECAIL)
    suspend fun getMovieSpecial(): Response<List<MovieDTO>>

    @GET(ApiRoutes.Movie.DETAIL)
    suspend fun getMovieDetail(@Path("userId") userId: Int, @Path("movieId") id: Int): Response<MovieDTO>

    @Multipart
    @POST(ApiRoutes.Movie.UPDATE)
    suspend fun updateMovie(
        @Part imagePoster: MultipartBody.Part?,
        @Part imageBanner: MultipartBody.Part?,
        @Part("movieId") movieId: RequestBody,
        @Part("type") type: RequestBody,
        @Part("status") status: RequestBody,
    ): Response<String>

    @GET(ApiRoutes.Movie.ALLGENRE)
    suspend fun getAllGenre(): Response<List<Genres>>

    @GET(ApiRoutes.Movie.FILTER)
    suspend fun filterMovie(
        @Query("searchText") searchText: String?,
        @Query("type") type: String?,
        @Query("year") year: Int?,
        @Query("status") status: String?,
        @Query("genres") genres: List<Int>?)
    : Response<List<MovieDTO>>

    @DELETE(ApiRoutes.Movie.DELETE)
    suspend fun deleteMovie(@Path("movieId") id: Int): Response<String>

}