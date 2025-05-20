package haui.do_an.moive_ticket_booking.api

import haui.do_an.moive_ticket_booking.DTO.CreditsResponse
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.DTO.SearchMovies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbAPIService {
    
    @GET(ApiRoutes.TMDB.SEARCH)
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<SearchMovies>

    @GET(ApiRoutes.TMDB.MOVIE_DETAIL)
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieTmdbDTO>

    @GET(ApiRoutes.TMDB.CREDITS)
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<CreditsResponse>

}