package haui.do_an.moive_ticket_booking.repository

import haui.do_an.moive_ticket_booking.api.TmdbAPIService
import haui.do_an.moive_ticket_booking.DTO.CreditsResponse
import haui.do_an.moive_ticket_booking.DTO.MovieTmdbDTO
import haui.do_an.moive_ticket_booking.DTO.SearchMovies
import java.util.Locale
import javax.inject.Inject

class TmdbRepository @Inject constructor(private val apiService: TmdbAPIService) {

    suspend fun searchMovie(query: String, apiKey: String): SearchMovies? {
        val response = apiService.searchMovie(query, apiKey)
        if (!response.isSuccessful) {
            throw Exception("Failed to search movies")
        }
        return response.body()
    }

    suspend fun getMovieDetail(movieId: Int, apiKey: String): MovieTmdbDTO? {
        val response = apiService.getMovieDetail(movieId, apiKey)
        if (!response.isSuccessful) {
            throw Exception("Failed to get movie detail")
        }
        val result = response.body()
        result?.release_date = result.release_date?.substring(0, 4)
        result?.language = result.language?.let { getLanguageNameInVietnamese(it) }

        return result
    }

    suspend fun getCredits(movieId: Int, apiKey: String): CreditsResponse? {
        val response = apiService.getCredits(movieId, apiKey)
        if (!response.isSuccessful) {
            throw Exception("Failed to get credits")
        }
        return response.body()
    }

    private fun getLanguageNameInVietnamese(languageCode: String): String {
        val locale = Locale(languageCode)
        return locale.getDisplayLanguage(Locale("vi"))
    }



}